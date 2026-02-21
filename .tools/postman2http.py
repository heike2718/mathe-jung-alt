#!/usr/bin/env python3
import json
import re
import sys
from pathlib import Path
from urllib.parse import urlparse

VAR_RE = re.compile(r"{{\s*([^}]+?)\s*}}")

def pm_var_to_restclient(s: str) -> str:
    # Keep {{var}} as-is; REST Client supports {{var}} when variables are defined in-file.
    return s

def normalize_headers(headers):
    # Postman headers may be list of dicts: [{"key": "...", "value": "...", "disabled": true/false}, ...]
    out = []
    if not headers:
        return out
    for h in headers:
        if not isinstance(h, dict):
            continue
        if h.get("disabled") is True:
            continue
        k = h.get("key")
        v = h.get("value")
        if not k or v is None:
            continue
        out.append((str(k).strip(), pm_var_to_restclient(str(v))))
    return out

def url_from_pm(url_obj):
    """
    url can be a string or an object:
    {
      "raw": "https://{{host}}/path?x=1",
      "protocol": "https",
      "host": ["{{host}}"],
      "path": ["path"],
      "query": [{"key":"x","value":"1"}]
    }
    """
    if url_obj is None:
        return ""
    if isinstance(url_obj, str):
        return pm_var_to_restclient(url_obj)
    if isinstance(url_obj, dict):
        raw = url_obj.get("raw")
        if raw:
            return pm_var_to_restclient(str(raw))
        protocol = url_obj.get("protocol") or ""
        host = url_obj.get("host") or []
        path = url_obj.get("path") or []
        query = url_obj.get("query") or []
        host_str = ".".join(host) if isinstance(host, list) else str(host)
        path_str = "/".join(path) if isinstance(path, list) else str(path)
        qparts = []
        if isinstance(query, list):
            for q in query:
                if not isinstance(q, dict):
                    continue
                if q.get("disabled") is True:
                    continue
                k = q.get("key")
                v = q.get("value")
                if not k:
                    continue
                if v is None:
                    qparts.append(str(k))
                else:
                    qparts.append(f"{k}={pm_var_to_restclient(str(v))}")
        q = ("?" + "&".join(qparts)) if qparts else ""
        if protocol:
            return f"{protocol}://{pm_var_to_restclient(host_str)}/{pm_var_to_restclient(path_str)}{q}".rstrip("/")
        # protocol missing: return host/path
        return f"{pm_var_to_restclient(host_str)}/{pm_var_to_restclient(path_str)}{q}".rstrip("/")
    return ""

def body_from_pm(body_obj):
    if not body_obj or not isinstance(body_obj, dict):
        return None
    mode = body_obj.get("mode")
    if mode == "raw":
        raw = body_obj.get("raw")
        if raw is None:
            return ""
        return pm_var_to_restclient(str(raw))
    if mode == "urlencoded":
        # Represent as application/x-www-form-urlencoded payload
        params = body_obj.get("urlencoded") or []
        parts = []
        for p in params:
            if not isinstance(p, dict):
                continue
            if p.get("disabled") is True:
                continue
            k = p.get("key")
            v = p.get("value", "")
            if k is None:
                continue
            parts.append(f"{k}={pm_var_to_restclient(str(v))}")
        return "&".join(parts)
    if mode == "formdata":
        # Multipart is not nicely representable; emit comment and skip
        return None
    return None

def request_name(item):
    return item.get("name") if isinstance(item, dict) else None

def iter_items(node, prefix=None):
    """
    Postman structure: { "item": [ folderOrRequest, ... ] }
    Folder has: { "name": "...", "item": [ ... ] }
    Request item has: { "name": "...", "request": {...} }
    """
    if prefix is None:
        prefix = []
    if not isinstance(node, dict):
        return
    items = node.get("item")
    if not isinstance(items, list):
        return
    for it in items:
        if not isinstance(it, dict):
            continue
        name = it.get("name") or ""
        if "request" in it:
            yield prefix + [name], it
        elif "item" in it:
            yield from iter_items(it, prefix + [name])

def to_http_blocks(collection):
    info = collection.get("info", {})
    col_name = info.get("name", "Postman Collection")
    blocks = [f"# {col_name}\n"]
    for path_parts, it in iter_items(collection):
        req = it.get("request", {})
        if not isinstance(req, dict):
            continue
        method = (req.get("method") or "GET").upper()
        url = url_from_pm(req.get("url"))
        headers = normalize_headers(req.get("header"))
        body = body_from_pm(req.get("body"))

        title = " / ".join([p for p in path_parts if p])
        blocks.append(f"### {title}\n")
        blocks.append(f"{method} {url}\n")
        for k, v in headers:
            blocks.append(f"{k}: {v}\n")
        if body is not None:
            blocks.append("\n")
            blocks.append(f"{body}\n")
        blocks.append("\n")
    return "".join(blocks)

def main():
    if len(sys.argv) < 3:
        print("Usage: postman2http.py <collection.json> <out.http>", file=sys.stderr)
        sys.exit(2)

    src = Path(sys.argv[1])
    dst = Path(sys.argv[2])

    data = json.loads(src.read_text(encoding="utf-8"))
    out = to_http_blocks(data)
    dst.write_text(out, encoding="utf-8")
    print(f"Wrote: {dst}")

if __name__ == "__main__":
    main()
