

export function isExpired(expiresAt: number): boolean {

    // expriesAt ist in Millisekunden seit 01.01.1970
    const nowMillis: number = new Date().getMilliseconds();
    return nowMillis > expiresAt;
}