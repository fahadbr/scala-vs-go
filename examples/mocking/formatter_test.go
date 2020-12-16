package format

import (
	"testing"

	"github.com/golang/mock/gomock"
	. "github.com/smartystreets/goconvey/convey"
)

//go:generates mockgen example.com/scalavsgo/examples/mocking Formatter > $PWD/formatter_mock.go

type Formatter interface {
	Format(s string) string
}

func sayHello(name string, formatter Formatter) string {
	return formatter.Format(name)
}

func TestSayHello(t *testing.T) {
	Convey("describe sayHello", t, func() {
		ctrl := gomock.NewController(t)
		formatter := NewMockFormatter(ctrl)
		answer := "Ah, Mr Bond. I've been expecting you"

		formatter.EXPECT().
			Format("Mr Bond").AnyTimes().
			Return(answer)

		Convey("it should respond with the appropriate response", func() {
			actual := sayHello("Mr Bond", formatter)
			So(actual, ShouldEqual, answer)
		})
	})

}
