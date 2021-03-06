[![Build Status](https://travis-ci.org/tools4j/decimal4j.svg?branch=master)](https://travis-ci.org/tools4j/decimal4j)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.decimal4j/decimal4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.decimal4j/decimal4j)

## decimal4j
Java library for fast fixed-point arithmetic based on longs with support for up to 18 decimal places.

#### Javadoc API
http://decimal4j.org/javadoc

#### Features
 - Fixed-point arithmetic with 0 to 18 decimal places
   - Implementation based on unscaled long value
   - Option to throw an exception when an arithmetic overflow occurs
 - Scale
   - Type of a variable defines the scale (except for wildcard types)
   - Result has usually the same scale as the primary operand (also for multiplication and division)
 - Type Conversion
   - Efficient conversion from and to various other number types
   - Convenience methods to directly inter-operate with other data types (long, double, ...)
   - All rounding modes supported (default: HALF_UP)
 - Efficiency
   - Fast and efficient implementation (see [performance benchmarks](https://github.com/tools4j/decimal4j/wiki/Performance))
   - [`MutableDecimal`](https://github.com/tools4j/decimal4j/wiki/Examples#example-3-mean-and-standard-deviation-with-mutabledecimal) implementation for chained operations
   - `DecimalArithmetic`  API for [zero-garbage computations](https://github.com/tools4j/decimal4j/wiki/DecimalArithmetic-API) (with unscaled long values)

#### Quick Start

###### Example 1: Interest Calculation
```java
public class Interest {
	public static void main(String[] args) {
		Decimal2f principal = Decimal2f.valueOf(9500);
		Decimal3f rate = Decimal3f.valueOf(0.067);
		Decimal2f time = Decimal2f.valueOf(1).divide(4);
		Decimal2f interest = principal.multiplyBy(rate.multiplyExact(time));
		System.out.println("First quarter interest:  $" + interest);
	}
}
```

will output
```
First quarter interest: $159.13
```

###### Example 2: Circumference of a Circle
```java
public class Circle {
	public static void main(String[] args) {
		Decimal18f PI = Decimal18f.valueOf(Math.PI);
		Decimal2f radius = Decimal2f.valueOf(5);
		Decimal2f circ = radius.multiplyBy(PI.multiply(2));
		System.out.println("Circumference with 5m radius is ~" + circ + "m");
		System.out.println("Circumference with 5m radius is ~" + (2*Math.PI * 5) + "m");

		Decimal2f down = radius.multiplyBy(PI.multiply(2), RoundingMode.DOWN);
		System.out.println("Circumference with 5m radius is larger than " + down + "m");
	}
}
```

will output
```
Circumference with 5m radius is ~31.42m
Circumference with 5m radius is ~31.41592653589793m
Circumference with 5m radius is larger than 31.41m
```

###### More examples
Can be found in the [wiki](https://github.com/tools4j/decimal4j/wiki/Examples).

#### Maven/Gradle

###### Maven
```xml
<dependency>
	<groupId>org.decimal4j</groupId>
	<artifactId>decimal4j</artifactId>
	<version>1.0.3</version>
	<scope>compile</scope>
</dependency>
```

###### Gradle
```
compile 'org.decimal4j:decimal4j:1.0.3'
```

#### Download
You can download binaries, sources and javadoc from maven central:
* [decimal4j download](http://search.maven.org/#artifactdetails%7Corg.decimal4j%7Cdecimal4j%7C1.0.3%7Cjar)

#### FAQ
* [Questions & Answers](https://github.com/tools4j/decimal4j/issues?utf8=%E2%9C%93&q=is%3Aissue+label%3Aquestion)

#### More Information
* [Javadoc API](http://decimal4j.org/javadoc)
* [Wiki](https://github.com/tools4j/decimal4j/wiki)
* [DoubleRounder Utility](https://github.com/tools4j/decimal4j/wiki/DoubleRounder-Utility): rounding double values to an arbitrary decimal precision from 0 to 18
* [More Examples](https://github.com/tools4j/decimal4j/wiki/Examples)
* [Zero-garbage Computations](https://github.com/tools4j/decimal4j/wiki/DecimalArithmetic-API) (with unscaled long values)
* [Test Coverage](https://github.com/tools4j/decimal4j/wiki/Test-Coverage)
* [Performance Benchmarks](https://github.com/tools4j/decimal4j/wiki/Performance)
