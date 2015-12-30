# Conversion into CNF



## What is CNF(Conjunctive Normal Form)?

## 

[Conjunctive normal form - Wikipedia](https://en.wikipedia.org/wiki/Conjunctive_normal_form)

## Conversion step

1.  Add brackets to match order of precedence
2.  Convert "if only if" into "implies"
3.  Replace "implies" by "and", "or", "not"
4.  Doing de morgan
5.  Doing distributive

## Input format

### Operator

*   neg .... "Negation"
*   and
*   or
*   imp ..... "implies"
*   iff ..... "if only if"
*   ()

### Symbol

Any word character but non-digit.  
ex.  
A and AA ....... ok  
A1 and B ....... not ok  

## Run Program

<pre>javac mainclass.java
java mainclass input.txt output.txt
</pre>
