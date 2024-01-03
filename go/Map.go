package main

func Map(vs []int, f func(int) int) []int {
	vsm := make([]int, len(vs))
	for i, v := range vs {
		vsm[i] = f(v)
	}
	return vsm
}

/*
example:
func add5(n int) int {
	return n + 5
}

func main() {
	var strs []int = {1, 2, 3}

	fmt.Println(Map(strs, add5))
}
*/
