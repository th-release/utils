import { Between } from "typeorm";

export function todayBetween() {
  const currentDate = new Date();
  const todayStart = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), 0, 0, 0);
  const todayEnd = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), 23, 59, 59);
  return Between(todayStart, todayEnd)
}

export function thisWeekBetween() {
  const currentDate = new Date();
  const currentDayOfWeek = currentDate.getDay();

  // 이번 주의 시작일 계산
  const startOfWeek = new Date(currentDate);
  startOfWeek.setDate(currentDate.getDate() - currentDayOfWeek + 1);
  startOfWeek.setHours(0, 0, 0, 0);

  // 이번 주의 끝일 계산
  const endOfWeek = new Date(startOfWeek);
  endOfWeek.setDate(startOfWeek.getDate() + 6);
  endOfWeek.setHours(23, 59, 59, 999);

  return Between(startOfWeek, endOfWeek);
}

export function thisMonthBetween(date?: string | Date) {
  const currentDate = new Date(date ? date : new Date());
  const startOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1, 0, 0, 0);
  const endOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0, 23, 59, 59);
  return Between(startOfMonth, endOfMonth);
}

export function thisYearBetween() {
  const currentDate = new Date();
  const startOfYear = new Date(currentDate.getFullYear(), 0, 1, 0, 0, 0);
  const endOfYear = new Date(currentDate.getFullYear() + 1, 0, 0, 23, 59, 59);
  return Between(startOfYear, endOfYear);
}
