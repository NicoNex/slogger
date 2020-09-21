# slogger
Simple Logger - Probably the most beautiful logging library you've ever seen.

## Back story
I had a very complex data flow to debug at work so I came up with this logging class to help me find even the most insidious bugs.
I wanted a simple-to-use logging library that allowed me to build very clear log messages even from many places in the code.

## Usage
Using SLogger is extremely easy.
To create a log message in one place you can do like this:
```java
SLogger.begin("myMethod")
		.field("WHEN", "Today")
		.field("LINE", "3")
		.newLine()
		.field("Nice Field", "Value")
		.line("This is just a simple line in the log")
		.print();
```
This will produce the following log message:
```
┌───────────────────────────────────────┐
│ 21-09-2020 12:40:37 - myMethod        │
├───────────────────────────────────────┤
│ WHEN: Today                           │
│ LINE: 3                               │
│                                       │
│ Nice Field: Value                     │
│ This is just a simple line in the log │
└───────────────────────────────────────┘
```

For a more complex case in which you want to log data from multiple functions or points in your code you can do like this:
```java
class MyClass {
	SLogger logger;

	void calledMethod() {
		logger = logger.line("We now are in the 'calledMethod' method!")
				.field("MyExampleField", "MyExampleValue");

	}

	void myMethod() {
		logger = SLogger.begin("My Awesome Title")
				.line("We are in the 'myMethod' method!")
				.field("METHOD", "myMethod")
				.newLine();

		calledMethod();

		logger.newLine()
				.line("End of the execution.")
				.print();
	}
}
```
This will produce the following log message:
```
┌──────────────────────────────────────────┐
│ 21-09-2020 12:47:16 - My Awesome Title   │
├──────────────────────────────────────────┤
│ We are in the 'myMethod' method!         │
│ METHOD: myMethod                         │
│                                          │
│ We now are in the 'calledMethod' method! │
│ MyExampleField: MyExampleValue           │
│                                          │
│ End of the execution.                    │
└──────────────────────────────────────────┘
```
Optionally it can use log4j to log in production code, just use the `log` method instead of the `print` one.
