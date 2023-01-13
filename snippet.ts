const snippet = (text: string, length: number) => {
  let str = text
  if (str.length > length) {
    str = str.substr(0, length - 2) + '...';
  }
  return str;
}

export default snippet;
