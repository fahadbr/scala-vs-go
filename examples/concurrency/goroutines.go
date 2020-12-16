package main

import (
	"fmt"
	"time"
)

func longRunningTask(from int, until int, resC chan []int) {
	fmt.Printf("generator inputs: from %v until %v\n", from, until)
	time.Sleep(2 * time.Second)

	results := make([]int, until-from+1)
	for i := from; i <= until; i++ {
		results[i-from] = i * 2
	}

	fmt.Printf("generated nums from %v to %v\n",
		results[0],
		results[len(results)-1],
	)
	resC <- results
}

func main() {
	var (
		c1 = make(chan []int)
		c2 = make(chan []int)
		c3 = make(chan []int)
	)

	go longRunningTask(0, 100, c1)
	go longRunningTask(100, 200, c2)

	r1, r2 := <-c1, <-c2
	go longRunningTask(r1[0], r2[len(r2)-1], c3)

	select {
	case r3 := <-c3:
		fmt.Printf("%+v\n", r3)
	case <-time.After(10 * time.Second):
		panic("timed out")
	}
}
