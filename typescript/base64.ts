function base64Encode(input: string | number): string {
    const str = input.toString();
    const buff = Buffer.from(str, 'utf-8');
    return buff.toString('base64');
}

function base64Decode(base64: string): string | number {
    const buff = Buffer.from(base64, 'base64');
    const str = buff.toString('utf-8');
    return isNaN(Number(str)) ? str : Number(str);
}

