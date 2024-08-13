import moment from "moment";

export function ldtConvert(arr: any[], foramt: string) {
  const date = new Date();

  date.setFullYear(arr[0], arr[1], arr[2])

  date.setHours(arr[3])
  date.setMinutes(arr[4])

  return moment(date).format(foramt)
}
