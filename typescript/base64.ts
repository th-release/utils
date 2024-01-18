function base64Encode(str: string): string {
    let buff = Buffer.from(str, 'utf-8');
    return buff.toString('base64');
}

function base64Decode(base64: string): string {
    let buff = Buffer.from(base64, 'base64');
    return buff.toString('utf-8');
}
