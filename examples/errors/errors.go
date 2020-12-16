package main

import (
	"fmt"
	"math"
	"strconv"
)

func parseInt(numString string) (int, error) {
	return strconv.Atoi(numString)
}

func getSqrt(i int) (int, error) {
	res := math.Sqrt(float64(i))

	if float64(int(res)) != res {
		return 0, fmt.Errorf("square root of %v is not a whole number", i)
	}

	return int(res), nil
}

func doWork(input string) (int, error) {
	i, err := parseInt(input)

	if err != nil {
		return 0, err
	}

	sqrt, err := getSqrt(i)
	if err != nil {
		return 0, err
	}

	return sqrt, nil
}

// doWork(100) -> (int, error) = (10, <nil>)
// doWork(100a) -> (int, error) = (0, strconv.Atoi: parsing "100a": invalid syntax)
// doWork(101) -> (int, error) = (0, square root of 101 is not a whole number)

func printResults(input string) {
	res, err := doWork(input)
	fmt.Printf("doWork(%v) -> (int, error) = (%v, %v)\n", input, res, err)
}

func main() {
	printResults("100")
	printResults("100a")
	printResults("101")
}
