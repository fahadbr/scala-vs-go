// vim:foldmethod=marker
package main

import (
	"fmt"
	"strings"
)

type Elem interface{}
type List interface {
	Cons(e Elem) List
	Head() Elem
}

type Nil struct{}

func (l Nil) Cons(e Elem) List { return Cons{head: e, tail: Nil{}} }
func (l Nil) Head() Elem       { return nil }

type Cons struct {
	head Elem
	tail List
}

func (l Cons) Cons(e Elem) List { return Cons{head: e, tail: l} }
func (l Cons) Head() Elem       { return l.head }

// With Wrappers {{{ //

func stringExample() string { return getHeadDoubled(Nil{}.Cons("c").Cons("b").Cons("a")) }

func intExample() string { return getHeadDoubled(Nil{}.Cons(3).Cons(2).Cons(1)) }

func getHeadUppercase(l List) string {
	if l.Head() != nil {
		return fmt.Sprintf("head is %v when upper cased", strings.ToUpper(l.Head().(string)))
	}
	return ""
}

func getHeadDoubled(l List) string {
	if l.Head() != nil {
		return fmt.Sprintf("head is %v when doubled", l.Head().(int)*2)
	}
	return ""
}

// stringExample -> error: interface conversion: main.Elem is string, not int
// intExample -> head is 2 when doubled

func NewStringList() StringList {
	return StringList{innerList: Nil{}}
}

type StringList struct {
	innerList List
}

func (s StringList) Cons(e string) StringList {
	return StringList{innerList: s.innerList.Cons(e)}
}

func (s StringList) Head() *string {
	if cons, ok := s.innerList.(Cons); ok {
		s := cons.Head().(string)
		return &s
	}
	return nil
}

func NewIntList() IntList {
	return IntList{innerList: Nil{}}
}

type IntList struct {
	innerList List
}

func (i IntList) Cons(e int) IntList {
	return IntList{innerList: i.innerList.Cons(e)}
}

func (i IntList) Head() *int {
	if cons, ok := i.innerList.(Cons); ok {
		i := cons.Head().(int)
		return &i
	}
	return nil
}

// }}} With Wrappers //

func stringExample2() string { return getHeadUppercase2(NewStringList().Cons("c").Cons("b").Cons("a")) }

func intExample2() string { return getHeadDoubled2(NewIntList().Cons(3).Cons(2).Cons(1)) }

func getHeadUppercase2(list StringList) string {
	if list.Head() != nil {
		return fmt.Sprintf("head is %v when upper cased", strings.ToUpper(*list.Head()))
	}
	return ""
}

func getHeadDoubled2(list IntList) string {
	if list.Head() != nil {
		return fmt.Sprintf("head is %v when doubled", *list.Head()*2)
	}
	return ""
}

// stringExample2 -> head is A when upper cased
// intExample2 -> head is 2 when doubled

func main() {
	run("stringExample", stringExample)
	run("intExample", intExample)
	run("stringExample2", stringExample2)
	run("intExample2", intExample2)
}

func run(name string, f func() string) {
	defer func() {
		if r := recover(); r != nil {
			fmt.Printf("%v -> error: %v\n", name, r)
		}
	}()

	fmt.Printf("%v -> %v\n", name, f())

}

// Covariance {{{ //

/*
 *func covariance() {
 *
 *  var i int = 42
 *  var a interface{} = i
 *  var intList []int = []int{1, 3, i}
 *  var anyList []interface{} = intList
 *  anyList[0] = "boogers"
 *
 *
 *  fmt.Println(i, a, intList, anyList)
 *}
 */

// }}} Covariance //
