export function calculateFileSize(size: number): string {

    let result = '';

    if (Math.round(size / 1024) < 2048) {
        result = Math.round(size / 1024) + ' kB';
    } else {
        result = Math.round(size / 1024 / 1024) + ' MB';
    }

    return result;
};

export function isValidFileName(fileName: string ) {

    if (!fileName || fileName.length === 0) {
        return true;
    }

    const regexp = new RegExp('^[\\da-zA-ZÄÖÜäöüß_\\-\\.]{1,100}$');
    return regexp.test(fileName);
};