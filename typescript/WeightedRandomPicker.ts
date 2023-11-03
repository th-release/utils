function getRandomWeightedItem(items: { name: string; weight: number }[]): string {
  const totalWeight = items.reduce((sum, item) => sum + item.weight, 0);
  const randomWeight = Math.random() * totalWeight;
  let accumulatedWeight = 0;

  for (const item of items) {
    accumulatedWeight += item.weight;
    if (randomWeight <= accumulatedWeight) {
      return item.name;
    }
  }
}


// test ------------------------------------------
// const items = [
//   { name: "아이템1", weight: 0.03 },
//   { name: "아이템2", weight: 5 },
//   { name: "아이템3", weight: 6 },
// ];

// const selected = getRandomWeightedItem(items);
// console.log(`선택된 아이템: ${selected}`);
