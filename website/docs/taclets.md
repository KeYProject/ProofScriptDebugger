
# Taclets
    
Generated on: Sat Sep 09 23:02:00 CEST 2017 by `gendoc.groovy`. 

Covering the *default* taclets of [KeY](http://key-project.org).

## FAKE_CLOSE

```
FAKE_CLOSE {
\closegoal\heuristics(obsolete)
Choices: {debug:on}}
```

## UNSOUND_ASSUME

```
UNSOUND_ASSUME {
\add [b]==>[] 
\heuristics(obsolete)
Choices: {debug:on}}
```

## abortJavaCardTransaction

```
abortJavaCardTransactionDiamond {
\find(==>diamond_transaction\[{ .. #abortJavaCardTransaction; ... }\] (post))
\replacewith([]==>[update-application(elem-update(heap)(anon(savedHeap,allObjects(java.lang.Object::<transactionConditionallyUpdated>),heap)),diamond(post))]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## abortJavaCardTransaction

```
abortJavaCardTransactionBox {
\find(==>box_transaction\[{ .. #abortJavaCardTransaction; ... }\] (post))
\replacewith([]==>[update-application(elem-update(heap)(anon(savedHeap,allObjects(java.lang.Object::<transactionConditionallyUpdated>),heap)),box(post))]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## abortJavaCardTransactionAPI

```
abortJavaCardTransactionAPI {
\find(==>#allmodal ( (modal operator))\[{ .. 
  #jcsystemType.#abortTransaction()@#jcsystemType; ... }\] (post))
\replacewith([]==>[#allmodal ( (modal operator))\[{ .. #abortJavaCardTransaction; ... }\] (post)]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## accDefinition

```
accDefinition {
\find(acc(h,s,o,o2))
\varcond(\notFreeIn(fv (variable), o2 (deltaObject term)), \notFreeIn(fv (variable), o (java.lang.Object term)), \notFreeIn(fv (variable), s (LocSet term)), \notFreeIn(fv (variable), h (Heap term)))
\replacewith(and(and(not(equals(o,null)),not(equals(o2,null))),exists{fv (variable)}(and(elementOf(o,fv,s),equals(deltaObject::select(h,o,fv),o2))))) 
\heuristics(simplify)
Choices: {reach:on}}
```

## activeUse

```
activeUseStaticFieldWriteAccess {
\find(#allmodal ( (modal operator))\[{ .. #sv=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess2 {
\find(#allmodal ( (modal operator))\[{ .. #v.#sv=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess3 {
\find(#allmodal ( (modal operator))\[{ .. #sv=#arr[#idx]; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #arr[#idx];static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess4 {
\find(#allmodal ( (modal operator))\[{ .. #v.#sv=#arr[#idx]; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #arr[#idx];static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess5 {
\find(#allmodal ( (modal operator))\[{ .. #sv=#v1.#a; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#a (program Variable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#a) #v0 = #v1.#a;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess6 {
\find(#allmodal ( (modal operator))\[{ .. #v.#sv=#v1.#a; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#a (program Variable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#a) #v0 = #v1.#a;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldReadAccess {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#sv; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. static-initialisation(#sv)#lhs=@(#sv); ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldReadAccess2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#v.#sv; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. static-initialisation(#sv)#lhs=@(#sv); ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess {
\find(#allmodal ( (modal operator))\[{ .. #sv=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess2 {
\find(#allmodal ( (modal operator))\[{ .. #v.#sv=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess3 {
\find(#allmodal ( (modal operator))\[{ .. #sv=#arr[#idx]; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #arr[#idx];@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess4 {
\find(#allmodal ( (modal operator))\[{ .. #v.#sv=#arr[#idx]; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #arr[#idx];@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess5 {
\find(#allmodal ( (modal operator))\[{ .. #sv=#v1.#a; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#a (program Variable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#a) #v0 = #v1.#a;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldWriteAccess6 {
\find(#allmodal ( (modal operator))\[{ .. #v.#sv=#v1.#a; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#a (program Variable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#a) #v0 = #v1.#a;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldReadAccess {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#sv; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=@(#sv); ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseStaticFieldReadAccess2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#v.#sv; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=@(#sv); ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseMultiplication {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left*#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left*#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseDivision {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left/#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left/#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseModulo {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left%#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left%#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseSubtraction {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left-#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left-#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseAddition {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left+#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left+#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseBitwiseAnd {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left&#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left&#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseBitwiseOr {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left|#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left|#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseBitwiseXOr {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left^#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left^#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseShiftRight {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left>>#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left>>#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseShiftLeft {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left<<#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left<<#right;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseUnsignedShiftRight {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left>>>#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left>>>#right; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseMinus {
\find(#allmodal ( (modal operator))\[{ .. #sv=-#left; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = -#left;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseBitwiseNegation {
\find(#allmodal ( (modal operator))\[{ .. #sv=~#left; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = ~#left;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseByteCast {
\find(#allmodal ( (modal operator))\[{ .. #sv=(byte)#seShortIntLong; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = (byte)#seShortIntLong;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseShortCast {
\find(#allmodal ( (modal operator))\[{ .. #sv=(short)#seIntLong; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = (short)#seIntLong;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseIntCast {
\find(#allmodal ( (modal operator))\[{ .. #sv=(int)#seLong; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = (int)#seLong;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseCharCast {
\find(#allmodal ( (modal operator))\[{ .. #sv=(char)#seByteShortIntLong; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = (char)#seByteShortIntLong;static-initialisation(#sv)@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseMultiplication {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left*#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left*#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseDivision {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left/#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left/#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseModulo {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left%#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left%#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseSubtraction {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left-#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left-#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseAddition {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left+#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left+#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseBitwiseAnd {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left&#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left&#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseBitwiseOr {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left|#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left|#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseBitwiseXOr {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left^#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left^#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseShiftRight {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left>>#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left>>#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseShiftLeft {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left<<#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left<<#right;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseUnsignedShiftRight {
\find(#allmodal ( (modal operator))\[{ .. #sv=#left>>>#right; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = #left>>>#right; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseUnaryMinus {
\find(#allmodal ( (modal operator))\[{ .. #sv=-#left; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = -#left;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseBitwiseNegation {
\find(#allmodal ( (modal operator))\[{ .. #sv=~#left; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = ~#left;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseByteCast {
\find(#allmodal ( (modal operator))\[{ .. #sv=(byte)#seShortIntLong; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = (byte)#seShortIntLong;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseShortCast {
\find(#allmodal ( (modal operator))\[{ .. #sv=(short)#seIntLong; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = (short)#seIntLong;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseIntCast {
\find(#allmodal ( (modal operator))\[{ .. #sv=(int)#seLong; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = (int)#seLong;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## activeUse

```
activeUseCharCast {
\find(#allmodal ( (modal operator))\[{ .. #sv=(char)#seByteShortIntLong; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#sv (program StaticVariable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#sv) #v0 = (char)#seByteShortIntLong;@(#sv)=#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## active_attribute_access

```
assignment_write_static_attribute_with_variable_prefix {
\find(#allmodal ( (modal operator))\[{ .. @(#v.#sv)=#se; ... }\] (post))
\add []==>[writePermission(Permission::select(permissions,#v,#memberPVToField(#sv)))] \replacewith(#allmodal ( (modal operator))\[{ .. assert false : "Access permission check-point (static write)."; ... }\] (post)) ;
\replacewith(update-application(elem-update(heap)(store(heap,#v,#memberPVToField(#sv),#se)),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## add_eq

```
add_eq {
\find(equals(i0,i1))
\replacewith(equals(add(i,i0),add(i,i1))) 

Choices: {}}
```

## add_eq_back

```
add_eq_back {
\find(equals(add(i1,i),add(i1,i0)))
\replacewith(equals(i,i0)) 

Choices: {}}
```

## add_eq_back

```
add_eq_back_2 {
\find(equals(add(i,i1),add(i0,i1)))
\replacewith(equals(i,i0)) 

Choices: {}}
```

## add_eq_back

```
add_eq_back_2_fst_comm {
\find(equals(add(i1,i),add(i0,i1)))
\replacewith(equals(i,i0)) 

Choices: {}}
```

## add_eq_back

```
add_eq_back_3 {
\find(equals(i1,add(i1,i0)))
\replacewith(equals(Z(0(#)),i0)) 

Choices: {}}
```

## add_equations

```
add_equations {
\assumes ([equals(i,i0)]==>[]) 
\find(equals(j,j0)==>)
\add [equals(add(i,j),add(i0,j0))]==>[] 

Choices: {}}
```

## add_equations_right

```
add_equations_right {
\assumes ([equals(i,i0)]==>[]) 
\find(==>equals(j,j0))
\add []==>[equals(add(i,j),add(i0,j0))] 

Choices: {}}
```

## add_less

```
add_less {
\find(lt(i,i0))
\replacewith(lt(add(i1,i),add(i1,i0))) 

Choices: {}}
```

## add_less_back

```
add_less_back {
\find(lt(add(i1,i),add(i1,i0)))
\replacewith(lt(i,i0)) 

Choices: {}}
```

## add_less_back

```
add_less_back_zero_1 {
\find(lt(i,add(i,i1)))
\replacewith(lt(Z(0(#)),i1)) 

Choices: {}}
```

## add_less_back

```
add_less_back_zero_1_comm {
\find(lt(i,add(i1,i)))
\replacewith(lt(Z(0(#)),i1)) 

Choices: {}}
```

## add_less_back

```
add_less_back_zero_2 {
\find(lt(add(i,i1),i))
\replacewith(lt(i1,Z(0(#)))) 

Choices: {}}
```

## add_less_back

```
add_less_back_zero_2_comm {
\find(lt(add(i1,i),i))
\replacewith(lt(i1,Z(0(#)))) 

Choices: {}}
```

## add_literals

```
add_literals {
\find(add(Z(iz),Z(jz)))
\replacewith(#add(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## add_non_neg_square

```
add_non_neg_square {
\add [geq(mul(squareFac,squareFac),Z(0(#)))]==>[] 
\heuristics(inEqSimp_nonNegSquares, inEqSimp_special_nonLin)
Choices: {}}
```

## add_sub_elim_left

```
add_sub_elim_left {
\find(add(neg(i),i))
\replacewith(Z(0(#))) 

Choices: {}}
```

## add_sub_elim_right

```
add_sub_elim_right {
\find(add(i,neg(i)))
\replacewith(Z(0(#))) 

Choices: {}}
```

## add_sub_step

```
add_sub_step {
\find(add(neg(i),neg(i0)))
\replacewith(neg(add(i,i0))) 

Choices: {}}
```

## add_two_inequations_1

```
add_two_inequations_1 {
\assumes ([lt(i,i0)]==>[]) 
\find(lt(j,j0)==>)
\add [lt(add(i,j),add(i0,j0))]==>[] 

Choices: {}}
```

## add_two_inequations_2

```
add_two_inequations_2 {
\assumes ([leq(i,i0)]==>[]) 
\find(leq(j,j0)==>)
\add [leq(add(i,j),add(i0,j0))]==>[] 

Choices: {}}
```

## add_zero_left

```
add_zero_left {
\find(add(Z(0(#)),i))
\replacewith(i) 
\heuristics(simplify_literals)
Choices: {}}
```

## add_zero_right

```
add_zero_right {
\find(add(i,Z(0(#))))
\replacewith(i) 
\heuristics(simplify_literals)
Choices: {}}
```

## addition

```
compound_addition_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse+#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v+#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## addition

```
compound_addition_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e+#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0+#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## addition

```
assignmentAdditionInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0+#seCharByteShortInt1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaAddInt(#seCharByteShortInt0,#seCharByteShortInt1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## addition

```
assignmentAdditionLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt+#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaAddLong(#seCharByteShortInt,#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## addition

```
assignmentAdditionLong2 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong+#seCharByteShortInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaAddLong(#seLong,#seCharByteShortInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## addition

```
assignmentAdditionLong3 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong0+#seLong1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaAddLong(#seLong0,#seLong1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## addition

```
assignmentAdditionBigint1 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seBigint+#seAny; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(add(#seBigint,#seAny)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## addition

```
assignmentAdditionBigint2 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seAny+#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(add(#seAny,#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## addition_associative

```
addition_associative {
\find(add(add(i0,i1),add(j0,j1)))
\replacewith(add(add(j0,i1),add(i0,j1))) 

Choices: {}}
```

## allFieldsAssignment

```
allFieldsUnfold {
\find(#allmodal ( (modal operator))\[{ .. #v=\all_fields(#nseObj); ... }\] (post))
\varcond(\new(#vObjNew (program Variable), \typeof(#nseObj (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseObj) #vObjNew = #nseObj;#v=\all_fields(#vObjNew); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## allFieldsEq

```
allFieldsEq {
\find(equals(allFields(o1),allFields(o2)))
\replacewith(equals(o1,o2)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## allFieldsSubsetOf

```
allFieldsSubsetOf {
\assumes ([subset(allFields(o),s)]==>[]) 
\find(elementOf(o,f,s))
\sameUpdateLevel\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## allLeft

```
allLeft {
\find(all{u (variable)}(b)==>)
\add [subst{u (variable)}(t,b)]==>[] 
\heuristics(gamma)
Choices: {}}
```

## allLeftHide

```
allLeftHide {
\find(all{u (variable)}(b)==>)
\addrules [insert_hidden {
\add [all{u (variable)}(b)]==>[] 

Choices: {}}] \replacewith([subst{u (variable)}(t,b)]==>[]) 
\heuristics(gamma_destructive)
Choices: {}}
```

## allObjectsAssignment

```
allObjectsAssignment {
\find(#allmodal ( (modal operator))\[{ .. #v=\all_objects(#eObj.#a); ... }\] (post))
\replacewith(update-application(elem-update(#v (program Variable))(allObjects(#memberPVToField(#a))),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## allRight

```
allRight {
\find(==>all{u (variable)}(b))
\replacewith([]==>[subst{u (variable)}(sk,b)]) 
\heuristics(delta)
Choices: {}}
```

## all_bool

```
all_bool {
\find(all{x (variable)}(c))
\replacewith(and(subst{x (variable)}(FALSE,c),subst{x (variable)}(TRUE,c))) 
\heuristics(boolean_cases)
Choices: {}}
```

## all_pull_out0

```
all_pull_out0 {
\find(and(all{u (variable)}(b),c))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(all{u (variable)}(and(b,c))) 
\heuristics(pullOutQuantifierAll)
Choices: {}}
```

## all_pull_out1

```
all_pull_out1 {
\find(and(c,all{u (variable)}(b)))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(all{u (variable)}(and(c,b))) 
\heuristics(pullOutQuantifierAll)
Choices: {}}
```

## all_pull_out2

```
all_pull_out2 {
\find(or(all{u (variable)}(b),c))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(all{u (variable)}(or(b,c))) 
\heuristics(pullOutQuantifierAll)
Choices: {}}
```

## all_pull_out3

```
all_pull_out3 {
\find(or(c,all{u (variable)}(b)))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(all{u (variable)}(or(c,b))) 
\heuristics(pullOutQuantifierAll)
Choices: {}}
```

## all_pull_out4

```
all_pull_out4 {
\find(and(all{u (variable)}(b),all{u2 (variable)}(c)))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(all{u (variable)}(and(b,subst{u2 (variable)}(u,c)))) 
\heuristics(pullOutQuantifierUnifying, pullOutQuantifierAll)
Choices: {}}
```

## all_unused

```
all_unused {
\find(all{u (variable)}(b))
\varcond(\notFreeIn(u (variable), b (formula)))
\replacewith(b) 
\heuristics(elimQuantifier)
Choices: {}}
```

## allocateInstance

```
allocateInstance {
\find(==>#allmodal ( (modal operator))\[{ .. 
  #lhs=#t.#allocate()@#t; ... }\] (post))
\varcond(\hasSort(#t2 (program Type), alphaObj), )
\add [and(and(and(not(equals(#lhs,null)),imp(wellFormed(heap),equals(boolean::select(heap,#lhs,java.lang.Object::<created>),FALSE))),imp(wellFormed(permissions),equals(boolean::select(permissions,#lhs,java.lang.Object::<created>),FALSE))),equals(alphaObj::exactInstance(#lhs),TRUE))]==>[] \replacewith([]==>[update-application(parallel-upd(elem-update(heap)(create(heap,#lhs)),elem-update(permissions)(create(permissions,#lhs))),#allmodal(post))]) 
\heuristics(method_expand)
Choices: {permissions:on,programRules:Java}}
```

## allocateInstance

```
allocateInstance {
\find(==>#allmodal ( (modal operator))\[{ .. 
  #lhs=#t.#allocate()@#t; ... }\] (post))
\varcond(\hasSort(#t2 (program Type), alphaObj), )
\add [and(and(not(equals(#lhs,null)),imp(wellFormed(heap),equals(boolean::select(heap,#lhs,java.lang.Object::<created>),FALSE))),equals(alphaObj::exactInstance(#lhs),TRUE))]==>[] \replacewith([]==>[update-application(elem-update(heap)(create(heap,#lhs)),#allmodal(post))]) 
\heuristics(method_expand)
Choices: {permissions:off,programRules:Java}}
```

## allocateInstanceWithLength

```
allocateInstanceWithLength {
\find(==>#allmodal ( (modal operator))\[{ .. 
  #lhs=#t.#allocate(#len)@#t; ... }\] (post))
\varcond(\hasSort(#t2 (program Type), alphaObj), )
\add [and(and(and(not(equals(#lhs,null)),imp(wellFormed(heap),and(equals(boolean::select(heap,#lhs,java.lang.Object::<created>),FALSE),equals(length(#lhs),#len)))),imp(wellFormed(permissions),equals(boolean::select(permissions,#lhs,java.lang.Object::<created>),FALSE))),equals(alphaObj::exactInstance(#lhs),TRUE))]==>[] \replacewith([]==>[update-application(parallel-upd(elem-update(heap)(store(store(create(heap,#lhs),#lhs,java.lang.Object::<transient>,Z(0(#))),#lhs,java.lang.Object::<transactionConditionallyUpdated>,FALSE)),elem-update(permissions)(create(permissions,#lhs))),#allmodal(post))]) 
\heuristics(method_expand)
Choices: {permissions:on,programRules:Java}}
```

## allocateInstanceWithLength

```
allocateInstanceWithLength {
\find(==>#allmodal ( (modal operator))\[{ .. 
  #lhs=#t.#allocate(#len)@#t; ... }\] (post))
\varcond(\hasSort(#t2 (program Type), alphaObj), )
\add [and(and(not(equals(#lhs,null)),imp(wellFormed(heap),and(equals(boolean::select(heap,#lhs,java.lang.Object::<created>),FALSE),equals(length(#lhs),#len)))),equals(alphaObj::exactInstance(#lhs),TRUE))]==>[] \replacewith([]==>[update-application(elem-update(heap)(store(store(create(heap,#lhs),#lhs,java.lang.Object::<transient>,Z(0(#))),#lhs,java.lang.Object::<transactionConditionallyUpdated>,FALSE)),#allmodal(post))]) 
\heuristics(method_expand)
Choices: {permissions:off,programRules:Java}}
```

## altAxiom

```
altAxiom {
\find(match(alt(rexp1,rexp2),string))
\replacewith(or(match(rexp1,string),match(rexp2,string))) 
\heuristics(simplify)
Choices: {Strings:on}}
```

## andJIntDef

```
andJIntDef {
\find(andJint(left,right))
\replacewith(moduloInt(binaryAnd(left,right))) 
\heuristics(simplify)
Choices: {}}
```

## andLeft

```
andLeft {
\find(and(b,c)==>)
\replacewith([b,c]==>[]) 
\heuristics(alpha)
Choices: {}}
```

## andRight

```
andRight {
\find(==>and(b,c))
\replacewith([]==>[c]) ;
\replacewith([]==>[b]) 
\heuristics(beta)
Choices: {}}
```

## applyEq

```
applyEq {
\assumes ([equals(s,t1)]==>[]) 
\find(s)
\sameUpdateLevel\replacewith(t1) 
\heuristics(apply_select_eq, apply_equations)
Choices: {}}
```

## applyEq

```
applyEqRigid {
\assumes ([equals(sr,tr1)]==>[]) 
\find(sr)
\replacewith(tr1) 
\heuristics(apply_equations)
Choices: {}}
```

## applyEqReverse

```
applyEqReverse {
\assumes ([equals(s,t1)]==>[]) 
\find(t1)
\sameUpdateLevel\replacewith(s) 
\heuristics(apply_auxiliary_eq)
Choices: {}}
```

## applyEq_and_gen0

```
applyEq_and_gen0 {
\find(and(equals(applyEqLeft,applyEqOther),equals(applyEqLeft,applyEqRight)))
\replacewith(and(equals(applyEqRight,applyEqOther),equals(applyEqLeft,applyEqRight))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_gen1

```
applyEq_and_gen1 {
\find(and(and(b,equals(applyEqLeft,applyEqOther)),equals(applyEqLeft,applyEqRight)))
\replacewith(and(and(b,equals(applyEqRight,applyEqOther)),equals(applyEqLeft,applyEqRight))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_gen2

```
applyEq_and_gen2 {
\find(and(not(equals(applyEqLeft,applyEqOther)),equals(applyEqLeft,applyEqRight)))
\replacewith(and(not(equals(applyEqRight,applyEqOther)),equals(applyEqLeft,applyEqRight))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_gen3

```
applyEq_and_gen3 {
\find(and(and(b,not(equals(applyEqLeft,applyEqOther))),equals(applyEqLeft,applyEqRight)))
\replacewith(and(and(b,not(equals(applyEqRight,applyEqOther))),equals(applyEqLeft,applyEqRight))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_int0

```
applyEq_and_int0 {
\find(and(geq(applyEqLeft,applyEqOther),equals(applyEqLeft,applyEqRight)))
\replacewith(and(geq(applyEqRight,applyEqOther),equals(applyEqLeft,applyEqRight))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_int1

```
applyEq_and_int1 {
\find(and(and(b,geq(applyEqLeft,applyEqOther)),equals(applyEqLeft,applyEqRight)))
\replacewith(and(and(b,geq(applyEqRight,applyEqOther)),equals(applyEqLeft,applyEqRight))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_int2

```
applyEq_and_int2 {
\find(and(leq(applyEqLeft,applyEqOther),equals(applyEqLeft,applyEqRight)))
\replacewith(and(leq(applyEqRight,applyEqOther),equals(applyEqLeft,applyEqRight))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_int3

```
applyEq_and_int3 {
\find(and(and(b,leq(applyEqLeft,applyEqOther)),equals(applyEqLeft,applyEqRight)))
\replacewith(and(and(b,leq(applyEqRight,applyEqOther)),equals(applyEqLeft,applyEqRight))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_int4

```
applyEq_and_int4 {
\find(and(equals(applyEqLeft,applyEqRight),geq(applyEqLeft,applyEqOther)))
\replacewith(and(equals(applyEqLeft,applyEqRight),geq(applyEqRight,applyEqOther))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_int5

```
applyEq_and_int5 {
\find(and(and(b,equals(applyEqLeft,applyEqRight)),geq(applyEqLeft,applyEqOther)))
\replacewith(and(and(b,equals(applyEqLeft,applyEqRight)),geq(applyEqRight,applyEqOther))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_int6

```
applyEq_and_int6 {
\find(and(equals(applyEqLeft,applyEqRight),leq(applyEqLeft,applyEqOther)))
\replacewith(and(equals(applyEqLeft,applyEqRight),leq(applyEqRight,applyEqOther))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_and_int7

```
applyEq_and_int7 {
\find(and(and(b,equals(applyEqLeft,applyEqRight)),leq(applyEqLeft,applyEqOther)))
\replacewith(and(and(b,equals(applyEqLeft,applyEqRight)),leq(applyEqRight,applyEqOther))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_gen0

```
applyEq_or_gen0 {
\find(or(equals(applyEqLeft,applyEqOther),not(equals(applyEqLeft,applyEqRight))))
\replacewith(or(equals(applyEqRight,applyEqOther),not(equals(applyEqLeft,applyEqRight)))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_gen1

```
applyEq_or_gen1 {
\find(or(or(b,equals(applyEqLeft,applyEqOther)),not(equals(applyEqLeft,applyEqRight))))
\replacewith(or(or(b,equals(applyEqRight,applyEqOther)),not(equals(applyEqLeft,applyEqRight)))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_gen2

```
applyEq_or_gen2 {
\find(or(not(equals(applyEqLeft,applyEqOther)),not(equals(applyEqLeft,applyEqRight))))
\replacewith(or(not(equals(applyEqRight,applyEqOther)),not(equals(applyEqLeft,applyEqRight)))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_gen3

```
applyEq_or_gen3 {
\find(or(or(b,not(equals(applyEqLeft,applyEqOther))),not(equals(applyEqLeft,applyEqRight))))
\replacewith(or(or(b,not(equals(applyEqRight,applyEqOther))),not(equals(applyEqLeft,applyEqRight)))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_int0

```
applyEq_or_int0 {
\find(or(geq(applyEqLeft,applyEqOther),not(equals(applyEqLeft,applyEqRight))))
\replacewith(or(geq(applyEqRight,applyEqOther),not(equals(applyEqLeft,applyEqRight)))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_int1

```
applyEq_or_int1 {
\find(or(or(b,geq(applyEqLeft,applyEqOther)),not(equals(applyEqLeft,applyEqRight))))
\replacewith(or(or(b,geq(applyEqRight,applyEqOther)),not(equals(applyEqLeft,applyEqRight)))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_int2

```
applyEq_or_int2 {
\find(or(leq(applyEqLeft,applyEqOther),not(equals(applyEqLeft,applyEqRight))))
\replacewith(or(leq(applyEqRight,applyEqOther),not(equals(applyEqLeft,applyEqRight)))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_int3

```
applyEq_or_int3 {
\find(or(or(b,leq(applyEqLeft,applyEqOther)),not(equals(applyEqLeft,applyEqRight))))
\replacewith(or(or(b,leq(applyEqRight,applyEqOther)),not(equals(applyEqLeft,applyEqRight)))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_int4

```
applyEq_or_int4 {
\find(or(not(equals(applyEqLeft,applyEqRight)),geq(applyEqLeft,applyEqOther)))
\replacewith(or(not(equals(applyEqLeft,applyEqRight)),geq(applyEqRight,applyEqOther))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_int5

```
applyEq_or_int5 {
\find(or(or(b,not(equals(applyEqLeft,applyEqRight))),geq(applyEqLeft,applyEqOther)))
\replacewith(or(or(b,not(equals(applyEqLeft,applyEqRight))),geq(applyEqRight,applyEqOther))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_int6

```
applyEq_or_int6 {
\find(or(not(equals(applyEqLeft,applyEqRight)),leq(applyEqLeft,applyEqOther)))
\replacewith(or(not(equals(applyEqLeft,applyEqRight)),leq(applyEqRight,applyEqOther))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyEq_or_int7

```
applyEq_or_int7 {
\find(or(or(b,not(equals(applyEqLeft,applyEqRight))),leq(applyEqLeft,applyEqOther)))
\replacewith(or(or(b,not(equals(applyEqLeft,applyEqRight))),leq(applyEqRight,applyEqOther))) 
\heuristics(apply_equations_andOr)
Choices: {}}
```

## applyOnElementary

```
applyOnElementary {
\find(update-application(u,elem-update(#pv (program Variable))(t)))
\replacewith(elem-update(#pv (program Variable))(update-application(u,t))) 
\heuristics(update_apply_on_update)
Choices: {}}
```

## applyOnPV

```
applyOnPV {
\find(update-application(elem-update(#pv (program Variable))(t),#pv))
\replacewith(t) 
\heuristics(update_elim)
Choices: {}}
```

## applyOnParallel

```
applyOnParallel {
\find(update-application(u,parallel-upd(u2,u3)))
\replacewith(parallel-upd(update-application(u,u2),update-application(u,u3))) 
\heuristics(update_apply_on_update)
Choices: {}}
```

## applyOnRigidFormula

```
applyOnRigidFormula {
\find(update-application(u,phi))
\varcond(\applyUpdateOnRigid(u (update), phi (formula), result (formula)), )
\replacewith(result) 
\heuristics(update_apply)
Choices: {}}
```

## applyOnRigidTerm

```
applyOnRigidTerm {
\find(update-application(u,t))
\varcond(\applyUpdateOnRigid(u (update), t (any term), result (any term)), )
\replacewith(result) 
\heuristics(update_apply)
Choices: {}}
```

## applyOnSkip

```
applyOnSkip {
\find(update-application(u,skip))
\replacewith(skip) 
\heuristics(update_elim)
Choices: {}}
```

## applySkip1

```
applySkip1 {
\find(update-application(skip,t))
\replacewith(t) 
\heuristics(concrete)
Choices: {}}
```

## applySkip2

```
applySkip2 {
\find(update-application(skip,phi))
\replacewith(phi) 
\heuristics(update_elim)
Choices: {}}
```

## applySkip3

```
applySkip3 {
\find(update-application(skip,u))
\replacewith(u) 
\heuristics(update_elim)
Choices: {}}
```

## apply_eq_boolean

```
apply_eq_boolean {
\assumes ([]==>[equals(bo,TRUE)]) 
\find(bo)
\sameUpdateLevel\replacewith(FALSE) 
\heuristics(apply_equations)
Choices: {}}
```

## apply_eq_boolean

```
apply_eq_boolean_2 {
\assumes ([]==>[equals(bo,FALSE)]) 
\find(bo)
\sameUpdateLevel\replacewith(TRUE) 
\heuristics(apply_equations)
Choices: {}}
```

## apply_eq_boolean

```
apply_eq_boolean_rigid {
\assumes ([]==>[equals(br,TRUE)]) 
\find(br)
\replacewith(FALSE) 
\heuristics(apply_equations)
Choices: {}}
```

## apply_eq_boolean

```
apply_eq_boolean_rigid_2 {
\assumes ([]==>[equals(br,FALSE)]) 
\find(br)
\replacewith(TRUE) 
\heuristics(apply_equations)
Choices: {}}
```

## apply_eq_monomials

```
apply_eq_monomials {
\assumes ([equals(applyEqDivisor,i0)]==>[]) 
\find(applyEqDividend)
\sameUpdateLevel\replacewith(add(mul(#divideMonomials(applyEqDividend,applyEqDivisor),add(i0,mul(applyEqDivisor,Z(neglit(1(#)))))),applyEqDividend)) 
\heuristics(notHumanReadable, apply_equations, polySimp_applyEq)
Choices: {}}
```

## apply_eq_monomials_rigid

```
apply_eq_monomials_rigid {
\assumes ([equals(applyEqDivisorr,i0r)]==>[]) 
\find(applyEqDividend)
\replacewith(add(mul(#divideMonomials(applyEqDividend,applyEqDivisorr),add(i0r,mul(applyEqDivisorr,Z(neglit(1(#)))))),applyEqDividend)) 
\heuristics(notHumanReadable, apply_equations, polySimp_applyEqRigid)
Choices: {}}
```

## apply_eq_pseudo_eq

```
apply_eq_pseudo_eq {
\assumes ([equals(mul(aePseudoLeft,aePseudoLeftCoeff),aePseudoRight)]==>[]) 
\find(equals(aePseudoTargetLeft,aePseudoTargetRight))
\sameUpdateLevel\replacewith(if-then-else(and(equals(aePseudoTargetLeft,mul(aePseudoLeft,aePseudoTargetFactor)),not(equals(aePseudoLeftCoeff,Z(0(#))))),equals(mul(aePseudoRight,aePseudoTargetFactor),mul(aePseudoTargetRight,aePseudoLeftCoeff)),equals(aePseudoTargetLeft,aePseudoTargetRight))) 
\heuristics(notHumanReadable, notHumanReadable, polySimp_applyEqPseudo, polySimp_leftNonUnit)
Choices: {}}
```

## apply_eq_pseudo_geq

```
apply_eq_pseudo_geq {
\assumes ([equals(mul(aePseudoLeft,aePseudoLeftCoeff),aePseudoRight)]==>[]) 
\find(geq(aePseudoTargetLeft,aePseudoTargetRight))
\sameUpdateLevel\replacewith(if-then-else(and(equals(aePseudoTargetLeft,mul(aePseudoLeft,aePseudoTargetFactor)),gt(aePseudoLeftCoeff,Z(0(#)))),geq(mul(aePseudoRight,aePseudoTargetFactor),mul(aePseudoTargetRight,aePseudoLeftCoeff)),geq(aePseudoTargetLeft,aePseudoTargetRight))) 
\heuristics(notHumanReadable, polySimp_applyEqPseudo, polySimp_leftNonUnit)
Choices: {}}
```

## apply_eq_pseudo_leq

```
apply_eq_pseudo_leq {
\assumes ([equals(mul(aePseudoLeft,aePseudoLeftCoeff),aePseudoRight)]==>[]) 
\find(leq(aePseudoTargetLeft,aePseudoTargetRight))
\sameUpdateLevel\replacewith(if-then-else(and(equals(aePseudoTargetLeft,mul(aePseudoLeft,aePseudoTargetFactor)),gt(aePseudoLeftCoeff,Z(0(#)))),leq(mul(aePseudoRight,aePseudoTargetFactor),mul(aePseudoTargetRight,aePseudoLeftCoeff)),leq(aePseudoTargetLeft,aePseudoTargetRight))) 
\heuristics(notHumanReadable, polySimp_applyEqPseudo, polySimp_leftNonUnit)
Choices: {}}
```

## apply_subst

```
apply_subst {
\find(subst{u (variable)}(t,target))
\replacewith(subst{u (variable)}(t,target)) 
\heuristics(try_apply_subst)
Choices: {}}
```

## apply_subst

```
apply_subst_for {
\find(subst{u (variable)}(t,phi))
\replacewith(subst{u (variable)}(t,phi)) 
\heuristics(try_apply_subst)
Choices: {}}
```

## array2seqDef

```
array2seqDef {
\find(array2seq(h,a))
\varcond(\notFreeIn(u (variable), h (Heap term)), \notFreeIn(u (variable), a (java.lang.Object term)))
\replacewith(seqDef{u (variable)}(Z(0(#)),length(a),any::select(h,a,arr(u)))) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## arrayCreation

```
arrayCreation {
\find(#normal ( (modal operator))\[{ .. #lhs=#na; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#na (program ArrayCreation))))
\replacewith(#normal ( (modal operator))\[{ .. #typeof(#na) #v0;init-array-creation(#na)#lhs=#v0; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## arrayCreationWithInitializers

```
arrayCreationWithInitializers {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#arrayinitializer; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;init-array-creation(#arrayinitializer)#lhs=#v0; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## arrayInitialisation

```
arrayInitialisation {
\find(#allmodal ( (modal operator))\[{ .. for ( int #v = #se; #v<this.#length; #v++ )
    this[#v]=#lit;
 ... }\] (post))
\replacewith(update-application(elem-update(heap)(memset(heap,arrayRange(#a,#se,sub(length(#a),Z(1(#)))),#lit)),#allmodal(post))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## arrayLengthIsAShort

```
arrayLengthIsAShort {
\find(length(o))
\sameUpdateLevel\add [inShort(length(o))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {JavaCard:on,programRules:Java}}
```

## arrayLengthIsAnInt

```
arrayLengthIsAnInt {
\find(length(o))
\sameUpdateLevel\add [inInt(length(o))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {JavaCard:off,programRules:Java}}
```

## arrayLengthNotNegative

```
arrayLengthNotNegative {
\find(length(o))
\sameUpdateLevel\add [geq(length(o),Z(0(#)))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {programRules:Java}}
```

## array_post_declaration

```
array_post_declaration {
\find(#allmodal ( (modal operator))\[{ .. #arraypost ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. array-post-declaration(#arraypost) ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## array_self_reference

```
array_self_reference {
\assumes ([wellFormed(heapSV)]==>[equals(array,null)]) 
\find(arrayStoreValid(array,G::select(heapSV,array,arr(idx))))
\sameUpdateLevel\replacewith(true) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## array_self_reference_eq

```
array_self_reference_eq {
\assumes ([wellFormed(heapSV),equals(G::select(heapSV,array,arr(idx)),EQ)]==>[equals(array,null)]) 
\find(arrayStoreValid(array,EQ))
\sameUpdateLevel\replacewith(true) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## assert

```
assert {
\find(#allmodal ( (modal operator))\[{ .. assert #se1; ... }\] (b))
\sameUpdateLevel\add [equals(#se1,FALSE)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.AssertionError (); ... }\] (b)) ;
\add [equals(#se1,TRUE)]==>[] \replacewith(#allmodal(b)) 
\heuristics(simplify_prog)
Choices: {assertions:on,programRules:Java}}
```

## assert

```
assertWithReferenceMessage {
\find(#allmodal ( (modal operator))\[{ .. assert #se1 : #se2; ... }\] (b))
\sameUpdateLevel\varcond(\isReference[non_null]( \typeof(#se2 (program SimpleExpression)) ), )
\add [equals(#se1,FALSE)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.AssertionError (#se2); ... }\] (b)) ;
\add [equals(#se1,TRUE)]==>[] \replacewith(#allmodal(b)) 
\heuristics(simplify_prog)
Choices: {assertions:on,programRules:Java}}
```

## assert

```
assertWithReferenceMessageNull {
\find(#allmodal ( (modal operator))\[{ .. assert #se1 : null; ... }\] (b))
\sameUpdateLevel\add [equals(#se1,FALSE)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.AssertionError ((java.lang.Object)null); ... }\] (b)) ;
\add [equals(#se1,TRUE)]==>[] \replacewith(#allmodal(b)) 
\heuristics(simplify_prog)
Choices: {assertions:on,programRules:Java}}
```

## assert

```
assertWithPrimitiveMessage {
\find(#allmodal ( (modal operator))\[{ .. assert #se1 : #se2; ... }\] (b))
\sameUpdateLevel\varcond(\not\isReference( \typeof(#se2 (program SimpleExpression)) ), )
\add [equals(#se1,FALSE)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.AssertionError (#se2); ... }\] (b)) ;
\add [equals(#se1,TRUE)]==>[] \replacewith(#allmodal(b)) 
\heuristics(simplify_prog)
Choices: {assertions:on,programRules:Java}}
```

## assert

```
assertSafe {
\find(==>#allmodal ( (modal operator))\[{ .. assert #e1; ... }\] (b))
\varcond(\new(#condition (program Variable), \typeof(#e1 (program Expression))), \not \containsAssignment( #e1 ), )
\add [equals(oldHeap,heap)]==>[] \replacewith([]==>[\[{method-frame(#ex): {#typeof    (#e1) #condition = #e1;
  }
}\] (all{f (variable)}(all{o (variable)}(or(and(not(equals(o,null)),equals(boolean::select(oldHeap,o,java.lang.Object::<created>),FALSE)),equals(any::select(oldHeap,o,f),any::select(heap,o,f))))))]) ;
\replacewith([]==>[\<{method-frame(#ex): {
    #condition=#e1;
  }
}\> (equals(#condition,TRUE))]) ;
\replacewith([]==>[#allmodal(b)]) 
\heuristics(simplify_prog)
Choices: {assertions:safe,programRules:Java}}
```

## assert

```
assertSafeWithMessage {
\find(==>#allmodal ( (modal operator))\[{ .. assert #e1 : #e2; ... }\] (b))
\varcond(\new(#message (program Variable), \typeof(#e2 (program Expression))), \new(#condition (program Variable), \typeof(#e1 (program Expression))), \not \containsAssignment( #e1 ), \not \containsAssignment( #e2 ), )
\add [equals(oldHeap,heap)]==>[] \replacewith([]==>[\[{method-frame(#ex): {#typeof    (#e1) #condition = #e1;#typeof    (#e2) #message = #e2;
  }
}\] (all{f (variable)}(all{o (variable)}(or(and(not(equals(o,null)),equals(boolean::select(oldHeap,o,java.lang.Object::<created>),FALSE)),equals(any::select(oldHeap,o,f),any::select(heap,o,f))))))]) ;
\replacewith([]==>[\<{method-frame(#ex): {#typeof    (#e1) #condition = #e1;#typeof    (#e2) #message = #e2;
  }
}\> (equals(#condition,TRUE))]) ;
\replacewith([]==>[#allmodal(b)]) 
\heuristics(simplify_prog)
Choices: {assertions:safe,programRules:Java}}
```

## assignment

```
assignment {
\find(#allmodal ( (modal operator))\[{ .. #loc=#se; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(#se),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignment

```
assignment_write_array_this_access_normalassign {
\find(#allmodal ( (modal operator))\[{ .. this[#se]=#se0; ... }\] (post))
\replacewith(imp(and(lt(#se,length(#v)),lt(Z(neglit(1(#))),#se)),update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),#allmodal(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {permissions:off,programRules:Java}}
```

## assignment

```
assignment_write_array_this_access_normalassign {
\find(#allmodal ( (modal operator))\[{ .. this[#se]=#se0; ... }\] (post))
\replacewith(imp(and(and(lt(#se,length(#v)),lt(Z(neglit(1(#))),#se)),writePermission(Permission::select(permissions,#v,arr(#se)))),update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),#allmodal(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {permissions:on,programRules:Java}}
```

## assignment

```
assignment_write_attribute {
\find(#allmodal ( (modal operator))\[{ .. #v.#a=#se; ... }\] (post))
\sameUpdateLevel\varcond( \not \static(#a (program Variable)), \not\isThisReference (#v (program Variable)), )
\add [equals(#v,null)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add []==>[writePermission(Permission::select(permissions,#v,#memberPVToField(#a)))] \replacewith(#allmodal ( (modal operator))\[{ .. assert false : "Access permission check-point (write)."; ... }\] (post)) ;
\add []==>[equals(#v,null)] \replacewith(update-application(elem-update(heap)(store(heap,#v,#memberPVToField(#a),#se)),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## assignment

```
assignment_write_attribute {
\find(==>#allmodal ( (modal operator))\[{ .. #v.#a=#se; ... }\] (post))
\varcond( \not \static(#a (program Variable)), \not\isThisReference (#v (program Variable)), )
\add [equals(#v,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[writePermission(Permission::select(permissions,#v,#memberPVToField(#a)))]) ;
\replacewith([]==>[update-application(elem-update(heap)(store(heap,#v,#memberPVToField(#a),#se)),#allmodal(post))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## assignment

```
assignment_write_attribute {
\find(#allmodal ( (modal operator))\[{ .. #v.#a=#se; ... }\] (post))
\varcond( \not \static(#a (program Variable)), )
\replacewith(update-application(elem-update(heap)(store(heap,#v,#memberPVToField(#a),#se)),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## assignment

```
assignment_write_static_attribute {
\find(#allmodal ( (modal operator))\[{ .. @(#sv)=#se; ... }\] (post))
\add []==>[writePermission(Permission::select(permissions,null,#memberPVToField(#sv)))] \replacewith(#allmodal ( (modal operator))\[{ .. assert false : "Access permission check-point (static write)."; ... }\] (post)) ;
\replacewith(update-application(elem-update(heap)(store(heap,null,#memberPVToField(#sv),#se)),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignment

```
assignment_read_static_attribute_with_variable_prefix {
\find(#allmodal ( (modal operator))\[{ .. #loc=@(#v.#sv); ... }\] (post))
\varcond(\hasSort(#sv (program StaticVariable), G), )
\add []==>[readPermission(Permission::select(permissions,#v,#memberPVToField(#sv)))] \replacewith(#allmodal ( (modal operator))\[{ .. assert false : "Access permission check-point (static read)."; ... }\] (post)) ;
\replacewith(update-application(elem-update(#loc (program Variable))(G::select(heap,#v,#memberPVToField(#sv))),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## assignmentSaveLocation

```
eval_order_array_access3 {
\find(#allmodal ( (modal operator))\[{ .. #v[#se]=#nse; ... }\] (post))
\varcond(\new(#v2 (program Variable), \typeof(#se (program SimpleExpression))), \new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#v (program Variable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#v) #v0 = #v;#typeof(#se) #v2 = #se;#typeof(#nse) #v1 = #nse;#v0[#v2]=#v1; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## assignmentSaveLocation

```
eval_order_array_access3 {
\find(#allmodal ( (modal operator))\[{ .. #v[#se]=#nse; ... }\] (post))
\varcond(\new(#v2 (program Variable), \typeof(#se (program SimpleExpression))), \new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#v (program Variable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#v) #v0 = #v;#typeof(#se) #v2 = #se;#typeof(#nse) #v1 = #nse;#v0[#v2]=#v1; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignmentSaveLocation

```
eval_order_access4 {
\find(#allmodal ( (modal operator))\[{ .. #v.#a=#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#v (program Variable))),  \not \static(#a (program Variable)), )
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#v) #v0 = #v;#typeof(#nse) #v1 = #nse;#v0.#a=#v1; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignmentSaveLocationThis

```
eval_order_access4_this {
\find(#allmodal ( (modal operator))\[{ .. #v.#a=#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))),  \not \static(#a (program Variable)), \isThisReference (#v (program Variable)), )
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v1 = #nse;#v.#a=#v1; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignmentThis

```
assignment_write_attribute_this {
\find(#allmodal ( (modal operator))\[{ .. #v.#a=#se; ... }\] (post))
\sameUpdateLevel\varcond( \not \static(#a (program Variable)), \isThisReference (#v (program Variable)), )
\add []==>[writePermission(Permission::select(permissions,#v,#memberPVToField(#a)))] \replacewith(#allmodal ( (modal operator))\[{ .. assert false : "Access permission check-point (write)."; ... }\] (post)) ;
\replacewith(update-application(elem-update(heap)(store(heap,#v,#memberPVToField(#a),#se)),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## assignmentThis

```
assignment_write_attribute_this {
\find(==>#allmodal ( (modal operator))\[{ .. #v.#a=#se; ... }\] (post))
\varcond( \not \static(#a (program Variable)), \isThisReference (#v (program Variable)), )
\replacewith([]==>[writePermission(Permission::select(permissions,#v,#memberPVToField(#a)))]) ;
\replacewith([]==>[update-application(elem-update(heap)(store(heap,#v,#memberPVToField(#a),#se)),#allmodal(post))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## assignmentUnfoldLeft

```
eval_order_array_access1 {
\find(#allmodal ( (modal operator))\[{ .. #nv[#e]=#e0; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nv (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nv) #v0 = #nv;#v0[#e]=#e0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignmentUnfoldLeft

```
eval_order_array_access2 {
\find(#allmodal ( (modal operator))\[{ .. #v[#nse]=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#ar1 (program Variable), \typeof(#v (program Variable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#v) #ar1 = #v;#typeof(#nse) #v0 = #nse;#ar1[#v0]=#e; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignmentUnfoldLeft

```
eval_order_access1 {
\find(#allmodal ( (modal operator))\[{ .. #nv.#attribute=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nv (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nv) #v0 = #nv;#v0.#attribute=#e; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignmentUnfoldRight

```
eval_order_array_access4 {
\find(#allmodal ( (modal operator))\[{ .. #v=#nv[#e]; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nv (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nv) #v0 = #nv;#v=#v0[#e]; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignmentUnfoldRight

```
eval_order_array_access5 {
\find(#allmodal ( (modal operator))\[{ .. #v=#v0[#nse]; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#ar1 (program Variable), \typeof(#v0 (program Variable))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#v0) #ar1 = #v0;#typeof(#nse) #v1 = #nse;#v=#ar1[#v1]; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignmentUnfoldRight

```
eval_order_array_access6 {
\find(#allmodal ( (modal operator))\[{ .. #v=#nv.#length; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nv (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nv) #v0 = #nv;#v=#v0.#length; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignmentUnfoldRight

```
eval_order_access2 {
\find(#allmodal ( (modal operator))\[{ .. #v=#nv.#attribute; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nv (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nv) #v0 = #nv;#v=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignment_and

```
compound_assignment_3_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#seBool0&&#seBool1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(equals(#seBool0,TRUE),if-then-else(equals(#seBool1,TRUE),TRUE,FALSE),FALSE)),#allmodal(post))) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## assignment_and

```
compound_assignment_4_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#seBool0&#seBool1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(equals(#seBool0,TRUE),if-then-else(equals(#seBool1,TRUE),TRUE,FALSE),FALSE)),#allmodal(post))) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## assignment_array2

```
assignment_array2 {
\find(#allmodal ( (modal operator))\[{ .. #v=#v0[#se]; ... }\] (post))
\sameUpdateLevel\varcond(\hasSort(\elemSort(#v0 (program Variable)), G), )
\add [and(not(equals(#v0,null)),or(leq(length(#v0),#se),lt(#se,Z(0(#)))))]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.ArrayIndexOutOfBoundsException (); ... }\] (post)) ;
\add [equals(#v0,null)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add []==>[readPermission(Permission::select(permissions,#v0,arr(#se)))] \replacewith(#allmodal ( (modal operator))\[{ .. assert false : "Access permission check-point (read)."; ... }\] (post)) ;
\add []==>[or(or(equals(#v0,null),leq(length(#v0),#se)),lt(#se,Z(0(#))))] \replacewith(update-application(elem-update(#v (program Variable))(G::select(heap,#v0,arr(#se))),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## assignment_array2

```
assignment_array2 {
\find(==>#allmodal ( (modal operator))\[{ .. #v=#v0[#se]; ... }\] (post))
\varcond(\hasSort(\elemSort(#v0 (program Variable)), G), )
\add [and(not(equals(#v0,null)),or(leq(length(#v0),#se),lt(#se,Z(0(#)))))]==>[] \replacewith([]==>[false]) ;
\add [equals(#v0,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[readPermission(Permission::select(permissions,#v0,arr(#se)))]) ;
\replacewith([]==>[update-application(elem-update(#v (program Variable))(G::select(heap,#v0,arr(#se))),#allmodal(post))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## assignment_array2

```
assignment_array2 {
\find(#allmodal ( (modal operator))\[{ .. #v=#v0[#se]; ... }\] (post))
\varcond(\hasSort(\elemSort(#v0 (program Variable)), G), )
\replacewith(update-application(elem-update(#v (program Variable))(G::select(heap,#v0,arr(#se))),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## assignment_negation

```
compound_assignment_1_new {
\find(#allmodal ( (modal operator))\[{ .. #lhs=!#seBool; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(equals(#seBool,TRUE),FALSE,TRUE)),#allmodal(post))) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## assignment_or

```
compound_assignment_5_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#seBool0||#seBool1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(equals(#seBool0,TRUE),TRUE,if-then-else(equals(#seBool1,TRUE),TRUE,FALSE))),#allmodal(post))) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## assignment_or

```
compound_assignment_6_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#seBool0|#seBool1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(equals(#seBool0,TRUE),TRUE,if-then-else(equals(#seBool1,TRUE),TRUE,FALSE))),#allmodal(post))) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## assignment_read_attribute

```
assignment_read_attribute {
\find(#allmodal ( (modal operator))\[{ .. #v0=#v.#a; ... }\] (post))
\sameUpdateLevel\varcond( \not \static(#a (program Variable)),  \not \isArrayLength(#a (program Variable)), \hasSort(#a (program Variable), G), \not\isThisReference (#v (program Variable)), )
\add [equals(#v,null)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add []==>[readPermission(Permission::select(permissions,#v,#memberPVToField(#a)))] \replacewith(#allmodal ( (modal operator))\[{ .. assert false : "Access permission check-point (read)."; ... }\] (post)) ;
\add []==>[equals(#v,null)] \replacewith(update-application(elem-update(#v0 (program Variable))(G::select(heap,#v,#memberPVToField(#a))),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## assignment_read_attribute

```
assignment_read_attribute {
\find(==>#allmodal ( (modal operator))\[{ .. #v0=#v.#a; ... }\] (post))
\varcond( \not \static(#a (program Variable)),  \not \isArrayLength(#a (program Variable)), \hasSort(#a (program Variable), G), \not\isThisReference (#v (program Variable)), )
\add [equals(#v,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[readPermission(Permission::select(permissions,#v,#memberPVToField(#a)))]) ;
\replacewith([]==>[update-application(elem-update(#v0 (program Variable))(G::select(heap,#v,#memberPVToField(#a))),#allmodal(post))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## assignment_read_attribute

```
assignment_read_attribute {
\find(#allmodal ( (modal operator))\[{ .. #v0=#v.#a; ... }\] (post))
\varcond( \not \static(#a (program Variable)),  \not \isArrayLength(#a (program Variable)), \hasSort(#a (program Variable), G), \not\isThisReference (#v (program Variable)), )
\replacewith(update-application(elem-update(#v0 (program Variable))(G::select(heap,#v,#memberPVToField(#a))),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## assignment_read_attribute_this

```
assignment_read_attribute_this {
\find(#allmodal ( (modal operator))\[{ .. #v0=#v.#a; ... }\] (post))
\sameUpdateLevel\varcond( \not \static(#a (program Variable)),  \not \isArrayLength(#a (program Variable)), \hasSort(#a (program Variable), G), \isThisReference (#v (program Variable)), )
\add []==>[readPermission(Permission::select(permissions,#v,#memberPVToField(#a)))] \replacewith(#allmodal ( (modal operator))\[{ .. assert false : "Access permission check-point (read)."; ... }\] (post)) ;
\replacewith(update-application(elem-update(#v0 (program Variable))(G::select(heap,#v,#memberPVToField(#a))),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## assignment_read_attribute_this

```
assignment_read_attribute_this {
\find(==>#allmodal ( (modal operator))\[{ .. #v0=#v.#a; ... }\] (post))
\varcond( \not \static(#a (program Variable)),  \not \isArrayLength(#a (program Variable)), \hasSort(#a (program Variable), G), \isThisReference (#v (program Variable)), )
\replacewith([]==>[readPermission(Permission::select(permissions,#v,#memberPVToField(#a)))]) ;
\replacewith([]==>[update-application(elem-update(#v0 (program Variable))(G::select(heap,#v,#memberPVToField(#a))),#allmodal(post))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## assignment_read_attribute_this

```
assignment_read_attribute_this {
\find(#allmodal ( (modal operator))\[{ .. #v0=#v.#a; ... }\] (post))
\varcond( \not \static(#a (program Variable)),  \not \isArrayLength(#a (program Variable)), \hasSort(#a (program Variable), G), \isThisReference (#v (program Variable)), )
\replacewith(update-application(elem-update(#v0 (program Variable))(G::select(heap,#v,#memberPVToField(#a))),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## assignment_read_length

```
assignment_read_length {
\find(#allmodal ( (modal operator))\[{ .. #v0=#v.#length; ... }\] (post))
\sameUpdateLevel\varcond(\not\isThisReference (#v (program Variable)), )
\add [equals(#v,null)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add []==>[equals(#v,null)] \replacewith(update-application(elem-update(#v0 (program Variable))(length(#v)),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## assignment_read_length

```
assignment_read_length {
\find(==>#allmodal ( (modal operator))\[{ .. #v0=#v.#length; ... }\] (post))
\varcond(\not\isThisReference (#v (program Variable)), )
\add [equals(#v,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[update-application(elem-update(#v0 (program Variable))(length(#v)),#allmodal(post))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## assignment_read_length

```
assignment_read_length {
\find(#allmodal ( (modal operator))\[{ .. #v0=#v.#length; ... }\] (post))
\varcond(\not\isThisReference (#v (program Variable)), )
\replacewith(update-application(elem-update(#v0 (program Variable))(length(#v)),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## assignment_read_length_this

```
assignment_read_length_this {
\find(#allmodal ( (modal operator))\[{ .. #v0=#v.#length; ... }\] (post))
\sameUpdateLevel\varcond(\isThisReference (#v (program Variable)), )
\replacewith(update-application(elem-update(#v0 (program Variable))(length(#v)),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignment_read_static_attribute

```
assignment_read_static_attribute {
\find(#allmodal ( (modal operator))\[{ .. #v0=@(#sv); ... }\] (post))
\sameUpdateLevel\varcond(\hasSort(#sv (program StaticVariable), G), )
\add []==>[readPermission(Permission::select(permissions,null,#memberPVToField(#sv)))] \replacewith(#allmodal ( (modal operator))\[{ .. assert false : "Access permission check-point (static read)."; ... }\] (post)) ;
\replacewith(update-application(elem-update(#v0 (program Variable))(G::select(heap,null,#memberPVToField(#sv))),#allmodal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## assignment_to_primitive_array_component

```
assignment_to_primitive_array_component {
\find(#normal ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\sameUpdateLevel\varcond( \not \isReferenceArray(#v (program Variable)), )
\add [and(not(equals(#v,null)),or(leq(length(#v),#se),lt(#se,Z(0(#)))))]==>[] \replacewith(#normal ( (modal operator))\[{ .. throw new java.lang.ArrayIndexOutOfBoundsException (); ... }\] (post)) ;
\add [equals(#v,null)]==>[] \replacewith(#normal ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add []==>[writePermission(Permission::select(permissions,#v,arr(#se)))] \replacewith(#normal ( (modal operator))\[{ .. assert false : "Access permission check-point (write)."; ... }\] (post)) ;
\add [and(and(not(equals(#v,null)),lt(#se,length(#v))),geq(#se,Z(0(#))))]==>[] \replacewith(update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),#normal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## assignment_to_primitive_array_component

```
assignment_to_primitive_array_component_transaction {
\find(#transaction ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\sameUpdateLevel\varcond( \not \isReferenceArray(#v (program Variable)), )
\add [and(not(equals(#v,null)),or(leq(length(#v),#se),lt(#se,Z(0(#)))))]==>[] \replacewith(#transaction ( (modal operator))\[{ .. throw new java.lang.ArrayIndexOutOfBoundsException (); ... }\] (post)) ;
\add [equals(#v,null)]==>[] \replacewith(#transaction ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add [and(and(not(equals(#v,null)),lt(#se,length(#v))),geq(#se,Z(0(#))))]==>[] \replacewith(update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),update-application(elem-update(savedHeap)(if-then-else(equals(int::select(heap,#v,java.lang.Object::<transient>),Z(0(#))),store(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>,TRUE),if-then-else(equals(boolean::select(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>),FALSE),store(savedHeap,#v,arr(#se),#se0),savedHeap))),#transaction(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {JavaCard:on,runtimeExceptions:allow,programRules:Java}}
```

## assignment_to_primitive_array_component

```
assignment_to_primitive_array_component {
\find(==>#normal ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\varcond( \not \isReferenceArray(#v (program Variable)), )
\add [and(not(equals(#v,null)),or(leq(length(#v),#se),lt(#se,Z(0(#)))))]==>[] \replacewith([]==>[false]) ;
\add [equals(#v,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[writePermission(Permission::select(permissions,#v,arr(#se)))]) ;
\replacewith([]==>[update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),#normal(post))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## assignment_to_primitive_array_component

```
assignment_to_primitive_array_component {
\find(#normal ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\varcond( \not \isReferenceArray(#v (program Variable)), )
\replacewith(update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),#normal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## assignment_to_primitive_array_component

```
assignment_to_primitive_array_component_transaction {
\find(==>#transaction ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\varcond( \not \isReferenceArray(#v (program Variable)), )
\add [and(not(equals(#v,null)),or(leq(length(#v),#se),lt(#se,Z(0(#)))))]==>[] \replacewith([]==>[false]) ;
\add [equals(#v,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),update-application(elem-update(savedHeap)(if-then-else(equals(int::select(heap,#v,java.lang.Object::<transient>),Z(0(#))),store(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>,TRUE),if-then-else(equals(boolean::select(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>),FALSE),store(savedHeap,#v,arr(#se),#se0),savedHeap))),#transaction(post)))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {JavaCard:on,runtimeExceptions:ban,programRules:Java}}
```

## assignment_to_primitive_array_component

```
assignment_to_primitive_array_component_transaction {
\find(#transaction ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\varcond( \not \isReferenceArray(#v (program Variable)), )
\replacewith(update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),update-application(elem-update(savedHeap)(if-then-else(equals(int::select(heap,#v,java.lang.Object::<transient>),Z(0(#))),store(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>,TRUE),if-then-else(equals(boolean::select(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>),FALSE),store(savedHeap,#v,arr(#se),#se0),savedHeap))),#transaction(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {JavaCard:on,runtimeExceptions:ignore,programRules:Java}}
```

## assignment_to_reference_array_component

```
assignment_to_reference_array_component {
\find(#normal ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\sameUpdateLevel\varcond(\isReferenceArray(#v (program Variable)), )
\add [and(and(and(not(equals(#v,null)),lt(#se,length(#v))),geq(#se,Z(0(#)))),not(arrayStoreValid(#v,#se0)))]==>[] \replacewith(#normal ( (modal operator))\[{ .. throw new java.lang.ArrayStoreException (); ... }\] (post)) ;
\add [and(not(equals(#v,null)),or(leq(length(#v),#se),lt(#se,Z(0(#)))))]==>[] \replacewith(#normal ( (modal operator))\[{ .. throw new java.lang.ArrayIndexOutOfBoundsException (); ... }\] (post)) ;
\add [equals(#v,null)]==>[] \replacewith(#normal ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add []==>[writePermission(Permission::select(permissions,#v,arr(#se)))] \replacewith(#normal ( (modal operator))\[{ .. assert false : "Access permission check-point (write)."; ... }\] (post)) ;
\add [and(and(and(not(equals(#v,null)),lt(#se,length(#v))),geq(#se,Z(0(#)))),arrayStoreValid(#v,#se0))]==>[] \replacewith(update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),#normal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## assignment_to_reference_array_component

```
assignment_to_reference_array_component_transaction {
\find(#transaction ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\sameUpdateLevel\varcond(\isReferenceArray(#v (program Variable)), )
\add [and(and(and(not(equals(#v,null)),lt(#se,length(#v))),geq(#se,Z(0(#)))),not(arrayStoreValid(#v,#se0)))]==>[] \replacewith(#transaction ( (modal operator))\[{ .. throw new java.lang.ArrayStoreException (); ... }\] (post)) ;
\add [and(not(equals(#v,null)),or(leq(length(#v),#se),lt(#se,Z(0(#)))))]==>[] \replacewith(#transaction ( (modal operator))\[{ .. throw new java.lang.ArrayIndexOutOfBoundsException (); ... }\] (post)) ;
\add [equals(#v,null)]==>[] \replacewith(#transaction ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add [and(and(and(not(equals(#v,null)),lt(#se,length(#v))),geq(#se,Z(0(#)))),arrayStoreValid(#v,#se0))]==>[] \replacewith(update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),update-application(elem-update(savedHeap)(if-then-else(equals(int::select(heap,#v,java.lang.Object::<transient>),Z(0(#))),store(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>,TRUE),if-then-else(equals(boolean::select(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>),FALSE),store(savedHeap,#v,arr(#se),#se0),savedHeap))),#transaction(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {JavaCard:on,runtimeExceptions:allow,programRules:Java}}
```

## assignment_to_reference_array_component

```
assignment_to_reference_array_component {
\find(==>#normal ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\varcond(\isReferenceArray(#v (program Variable)), )
\add [and(and(and(not(equals(#v,null)),lt(#se,length(#v))),geq(#se,Z(0(#)))),not(arrayStoreValid(#v,#se0)))]==>[] \replacewith([]==>[false]) ;
\add [and(not(equals(#v,null)),or(leq(length(#v),#se),lt(#se,Z(0(#)))))]==>[] \replacewith([]==>[false]) ;
\add [equals(#v,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[writePermission(Permission::select(permissions,#v,arr(#se)))]) ;
\replacewith([]==>[update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),#normal(post))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## assignment_to_reference_array_component

```
assignment_to_reference_array_component {
\find(#normal ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\varcond(\isReferenceArray(#v (program Variable)), )
\replacewith(update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),#normal(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## assignment_to_reference_array_component

```
assignment_to_reference_array_component_transaction {
\find(==>#transaction ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\varcond(\isReferenceArray(#v (program Variable)), )
\add [and(and(and(not(equals(#v,null)),lt(#se,length(#v))),geq(#se,Z(0(#)))),not(arrayStoreValid(#v,#se0)))]==>[] \replacewith([]==>[false]) ;
\add [and(not(equals(#v,null)),or(leq(length(#v),#se),lt(#se,Z(0(#)))))]==>[] \replacewith([]==>[false]) ;
\add [equals(#v,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),update-application(elem-update(savedHeap)(if-then-else(equals(int::select(heap,#v,java.lang.Object::<transient>),Z(0(#))),store(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>,TRUE),if-then-else(equals(boolean::select(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>),FALSE),store(savedHeap,#v,arr(#se),#se0),savedHeap))),#transaction(post)))]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {JavaCard:on,runtimeExceptions:ban,programRules:Java}}
```

## assignment_to_reference_array_component

```
assignment_to_reference_array_component_transaction {
\find(#transaction ( (modal operator))\[{ .. #v[#se]=#se0; ... }\] (post))
\varcond(\isReferenceArray(#v (program Variable)), )
\replacewith(update-application(elem-update(heap)(store(heap,#v,arr(#se),#se0)),update-application(elem-update(savedHeap)(if-then-else(equals(int::select(heap,#v,java.lang.Object::<transient>),Z(0(#))),store(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>,TRUE),if-then-else(equals(boolean::select(savedHeap,#v,java.lang.Object::<transactionConditionallyUpdated>),FALSE),store(savedHeap,#v,arr(#se),#se0),savedHeap))),#transaction(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {JavaCard:on,runtimeExceptions:ignore,programRules:Java}}
```

## assignment_xor

```
compound_assignment_xor_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#seBool0^#seBool1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(equals(#seBool0,#seBool1),FALSE,TRUE)),#allmodal(post))) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## associativeLawIntersect

```
associativeLawIntersect {
\find(intersect(s1,intersect(s2,s3)))
\replacewith(intersect(intersect(s1,s2),s3)) 
\heuristics(conjNormalForm)
Choices: {programRules:Java}}
```

## associativeLawUnion

```
associativeLawUnion {
\find(union(s1,union(s2,s3)))
\replacewith(union(union(s1,s2),s3)) 
\heuristics(conjNormalForm)
Choices: {programRules:Java}}
```

## autoInduct_Lemma

```
autoInduct_Lemma {
\find(==>and(all{uSub (variable)}(b),phi))
\replacewith([all{uSub (variable)}(b)]==>[phi]) ;
\replacewith([]==>[imp(and(leq(sk,Z(0(#))),subst{uSub (variable)}(sk,b)),subst{uSub (variable)}(sub(sk,Z(1(#))),b))]) ;
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(sk,b)),subst{uSub (variable)}(add(sk,Z(1(#))),b))]) ;
\replacewith([]==>[subst{uSub (variable)}(Z(0(#)),b)]) 

Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_geqZero {
\find(==>all{uSub (variable)}(b))
\replacewith([]==>[imp(and(leq(sk,Z(0(#))),subst{uSub (variable)}(sk,b)),subst{uSub (variable)}(sub(sk,Z(1(#))),b))]) ;
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(sk,b)),subst{uSub (variable)}(add(sk,Z(1(#))),b))]) ;
\replacewith([]==>[subst{uSub (variable)}(Z(0(#)),b)]) 

Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_geq_1 {
\find(==>all{uSub (variable)}(imp(leq(t,uSub),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,b),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_geq_2 {
\find(==>all{uSub (variable)}(or(gt(t,uSub),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,b),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_geq_3 {
\find(==>all{uSub (variable)}(or(lt(uSub,t),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,b),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_geq_5 {
\find(==>all{uSub (variable)}(or(or(gt(t,uSub),psi),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),or(psi,b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),or(psi,b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,or(psi,b)),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_geq_6 {
\find(==>all{uSub (variable)}(or(or(lt(uSub,t),psi),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),or(psi,b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),or(psi,b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,or(psi,b)),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_gt_1 {
\find(==>all{uSub (variable)}(imp(lt(t,uSub),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),b),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_gt_2 {
\find(==>all{uSub (variable)}(or(geq(t,uSub),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),b),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_gt_3 {
\find(==>all{uSub (variable)}(or(leq(uSub,t),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),b),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_gt_5 {
\find(==>all{uSub (variable)}(or(or(geq(t,uSub),psi),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),or(psi,b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),or(psi,b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),or(psi,b)),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_gt_6 {
\find(==>all{uSub (variable)}(or(or(leq(uSub,t),psi),b)))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),or(psi,b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),or(psi,b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),or(psi,b)),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_geqZeroLeft {
\find(exists{uSub (variable)}(b)==>)
\replacewith([]==>[imp(and(leq(sk,Z(0(#))),subst{uSub (variable)}(sk,not(b))),subst{uSub (variable)}(sub(sk,Z(1(#))),not(b)))]) ;
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(sk,not(b))),subst{uSub (variable)}(add(sk,Z(1(#))),not(b)))]) ;
\replacewith([]==>[subst{uSub (variable)}(Z(0(#)),not(b))]) 

Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_geq_Left1 {
\find(exists{uSub (variable)}(and(leq(t,uSub),b))==>)
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),not(b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),not(b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,not(b)),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_geq_Left2 {
\find(exists{uSub (variable)}(and(geq(uSub,t),b))==>)
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),not(b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),not(b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,not(b)),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_gt_Left1 {
\find(exists{uSub (variable)}(and(lt(t,uSub),b))==>)
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),not(b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),not(b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),not(b)),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction

```
auto_int_induction_gt_Left2 {
\find(exists{uSub (variable)}(and(gt(uSub,t),b))==>)
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),not(b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),not(b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),not(b)),true)]) 
\heuristics(induction_var, auto_induction)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGEQ_Lemma_1 {
\find(==>and(all{uSub (variable)}(imp(leq(t,uSub),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(leq(t,uSub),b))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,b),true)]) 
\heuristics(induction_var, auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGEQ_Lemma_2 {
\find(==>and(all{uSub (variable)}(or(gt(t,uSub),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(leq(t,uSub),b))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,b),true)]) 
\heuristics(induction_var, auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGEQ_Lemma_3 {
\find(==>and(all{uSub (variable)}(or(lt(uSub,t),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(leq(t,uSub),b))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,b),true)]) 
\heuristics(induction_var, auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGEQ_Lemma_5 {
\find(==>and(all{uSub (variable)}(or(or(gt(t,uSub),psi),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(leq(t,uSub),or(psi,b)))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),or(psi,b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),or(psi,b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,or(psi,b)),true)]) 
\heuristics(induction_var, auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGEQ_Lemma_6 {
\find(==>and(all{uSub (variable)}(or(or(lt(uSub,t),psi),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(leq(t,uSub),or(psi,b)))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(0(#))),subst{uSub (variable)}(add(t,sk),or(psi,b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),or(psi,b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(t,or(psi,b)),true)]) 
\heuristics(induction_var, auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGT_Lemma_1 {
\find(==>and(all{uSub (variable)}(imp(lt(t,uSub),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(lt(t,uSub),b))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),b),true)]) 
\heuristics(induction_var, auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGT_Lemma_2 {
\find(==>and(all{uSub (variable)}(or(geq(t,uSub),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(lt(t,uSub),b))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),b),true)]) 
\heuristics(induction_var, auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGT_Lemma_3 {
\find(==>and(all{uSub (variable)}(or(leq(uSub,t),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(lt(t,uSub),b))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),b)),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),b),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),b),true)]) 
\heuristics(induction_var, auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGT_Lemma_5 {
\find(==>and(all{uSub (variable)}(or(or(geq(t,uSub),psi),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(lt(t,uSub),or(psi,b)))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),or(psi,b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),or(psi,b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),or(psi,b)),true)]) 
\heuristics(auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## auto_induction_lemma

```
autoInductGT_Lemma_6 {
\find(==>and(all{uSub (variable)}(or(or(leq(uSub,t),psi),b)),phi))
\varcond(\notFreeIn(uSub (variable), t (int term)))
\replacewith([all{uSub (variable)}(imp(lt(t,uSub),or(psi,b)))]==>[phi]) ;
\replacewith([]==>[imp(and(geq(sk,Z(1(#))),subst{uSub (variable)}(add(t,sk),or(psi,b))),#ExpandQueries(subst{uSub (variable)}(add(t,add(sk,Z(1(#)))),or(psi,b)),true))]) ;
\replacewith([]==>[#ExpandQueries(subst{uSub (variable)}(add(t,Z(1(#))),or(psi,b)),true)]) 
\heuristics(induction_var, auto_induction_lemma)
Choices: {integerSimplificationRules:full}}
```

## beginJavaCardTransaction

```
beginJavaCardTransactionDiamond {
\find(==>\<{ .. #beginJavaCardTransaction; ... }\> (post))
\replacewith([]==>[update-application(elem-update(savedHeap)(heap),diamond_transaction(post))]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## beginJavaCardTransaction

```
beginJavaCardTransactionBox {
\find(==>\[{ .. #beginJavaCardTransaction; ... }\] (post))
\replacewith([]==>[update-application(elem-update(savedHeap)(heap),box_transaction(post))]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## beginJavaCardTransactionAPI

```
beginJavaCardTransactionAPI {
\find(==>#allmodal ( (modal operator))\[{ .. 
  #jcsystemType.#beginTransaction()@#jcsystemType; ... }\] (post))
\replacewith([]==>[#allmodal ( (modal operator))\[{ .. #beginJavaCardTransaction; ... }\] (post)]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## binaryAndOne

```
binaryAndOne {
\find(binaryAnd(left,Z(1(#))))
\replacewith(if-then-else(equals(mod(left,Z(2(#))),Z(0(#))),Z(0(#)),Z(1(#)))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## binaryAndSymm

```
binaryAndSymm {
\find(binaryAnd(left,right))
\replacewith(binaryAnd(right,left)) 

Choices: {}}
```

## binaryAndZeroLeft

```
binaryAndZeroLeft {
\find(binaryAnd(Z(0(#)),right))
\replacewith(Z(0(#))) 
\heuristics(concrete)
Choices: {}}
```

## binaryAndZeroRight

```
binaryAndZeroRight {
\find(binaryAnd(left,Z(0(#))))
\replacewith(Z(0(#))) 
\heuristics(concrete)
Choices: {}}
```

## binaryOrGte

```
binaryOrGte {
\find(binaryOr(left,right))
\sameUpdateLevel\add [imp(and(geq(left,Z(0(#))),geq(right,Z(0(#)))),and(and(geq(binaryOr(left,right),left),geq(binaryOr(left,right),right)),leq(binaryOr(left,right),mul(Z(2(#)),if-then-else(gt(left,right),left,right)))))]==>[] 
\heuristics(userTaclets1)
Choices: {}}
```

## binaryOrInInt

```
binaryOrInInt {
\find(binaryOr(left,right))
\sameUpdateLevel\add [imp(and(inInt(left),inInt(right)),inInt(binaryOr(left,right)))]==>[] 
\heuristics(userTaclets1)
Choices: {}}
```

## binaryOrNeutralLeft

```
binaryOrNeutralLeft {
\find(binaryOr(Z(0(#)),right))
\replacewith(right) 
\heuristics(concrete)
Choices: {}}
```

## binaryOrNeutralRight

```
binaryOrNeutralRight {
\find(binaryOr(left,Z(0(#))))
\replacewith(left) 
\heuristics(concrete)
Choices: {}}
```

## binaryOrSign

```
binaryOrSign {
\find(binaryOr(left,right))
\sameUpdateLevel\add [geq(mul(if-then-else(and(geq(left,Z(0(#))),geq(right,Z(0(#)))),Z(1(#)),Z(neglit(1(#)))),binaryOr(left,right)),Z(0(#)))]==>[] 
\heuristics(userTaclets1)
Choices: {}}
```

## binary_AND

```
binaryAnd_literals {
\find(binaryAnd(Z(iz),Z(jz)))
\replacewith(#BinaryAnd(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## binary_AND

```
compound_binary_AND_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse&#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v&#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## binary_AND

```
compound_binary_AND_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e&#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0&#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## binary_OR

```
binaryOr_literals {
\find(binaryOr(Z(iz),Z(jz)))
\replacewith(#BinaryOr(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## binary_OR

```
compound_binary_OR_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse|#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v|#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## binary_OR

```
compound_binary_OR_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e|#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0|#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## binary_XOR

```
binaryXOr_literals {
\find(binaryXOr(Z(iz),Z(jz)))
\replacewith(#BinaryXOr(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## binary_XOR

```
compound_binary_XOR_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse^#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v^#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## binary_XOR

```
compound_binary_XOR_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e^#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0^#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## bitwiseAnd

```
assignmentBitwiseAndInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0&#seCharByteShortInt1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseAndInt(#seCharByteShortInt0,#seCharByteShortInt1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseAnd

```
assignmentBitwiseAndLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt&#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseAndLong(#seCharByteShortInt,#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseAnd

```
assignmentBitwiseAndLong2 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong&#seCharByteShortInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseAndLong(#seLong,#seCharByteShortInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseAnd

```
assignmentBitwiseAndLong3 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong0&#seLong1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseAndLong(#seLong0,#seLong1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseOr

```
assignmentBitwiseOrInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0|#seCharByteShortInt1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseOrInt(#seCharByteShortInt0,#seCharByteShortInt1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseOr

```
assignmentBitwiseOrLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt|#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseOrLong(#seCharByteShortInt,#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseOr

```
assignmentBitwiseOrLong2 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong|#seCharByteShortInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseOrLong(#seLong,#seCharByteShortInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseOr

```
assignmentBitwiseOrLong3 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong0|#seLong1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseOrLong(#seLong0,#seLong1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseXOr

```
assignmentBitwiseXOrInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0^#seCharByteShortInt1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseXOrInt(#seCharByteShortInt0,#seCharByteShortInt1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseXOr

```
assignmentBitwiseXOrLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt^#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseXOrLong(#seCharByteShortInt,#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseXOr

```
assignmentBitwiseXOrLong2 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong^#seCharByteShortInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseXOrLong(#seLong,#seCharByteShortInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## bitwiseXOr

```
assignmentBitwiseXOrLong3 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong0^#seLong1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseXOrLong(#seLong0,#seLong1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## blockBreakLabel

```
blockBreakLabel {
\find(#allmodal ( (modal operator))\[{ .. #lb0: {break ;
    #slist
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. do-break(#lb0:break ;  ) ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## blockBreakNoLabel

```
blockBreakNoLabel {
\find(#allmodal ( (modal operator))\[{ ..  {break ;
    #slist
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. break ;
 ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## blockEmpty

```
blockEmptyLabel {
\find(#allmodal ( (modal operator))\[{ .. #lb: {} ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {} ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## blockEmpty

```
blockEmpty {
\find(#allmodal ( (modal operator))\[{ ..  {} ... }\] (post))
\replacewith(#allmodal(post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## blockReturn

```
blockReturnNoValue {
\find(#allmodal ( (modal operator))\[{ ..  {return ;#slist} ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. return ; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## blockReturn

```
blockReturn {
\find(#allmodal ( (modal operator))\[{ ..  {return #se;#slist} ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. return #se; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## blockReturn (remove label)

```
blockReturnLabel1 {
\find(#allmodal ( (modal operator))\[{ .. #lb:return #se; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. return #se; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## blockReturn (remove label)

```
blockReturnLabel2 {
\find(#allmodal ( (modal operator))\[{ .. #lb: {return #se;#slist} ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. return #se; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## blockThrow

```
blockThrow {
\find(#allmodal ( (modal operator))\[{ ..  {throw #e;#slist} ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. throw #e; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## boolean commute

```
boolean_true_commute {
\find(equals(TRUE,bo))
\replacewith(equals(bo,TRUE)) 
\heuristics(simplify_boolean)
Choices: {}}
```

## boolean commute

```
boolean_false_commute {
\find(equals(FALSE,bo))
\replacewith(equals(bo,FALSE)) 
\heuristics(simplify_boolean)
Choices: {}}
```

## boolean_equal

```
boolean_equal {
\find(equals(bo,bo))
\replacewith(true) 
\heuristics(concrete, simplify_boolean)
Choices: {}}
```

## boolean_equal

```
boolean_equal_2 {
\find(equiv(equals(b1,TRUE),equals(b2,TRUE)))
\replacewith(equals(b1,b2)) 
\heuristics(concrete, simplify_boolean)
Choices: {}}
```

## boolean_not_equal

```
boolean_not_equal_1 {
\find(equals(TRUE,FALSE))
\replacewith(false) 
\heuristics(concrete, simplify_boolean)
Choices: {}}
```

## boolean_not_equal

```
boolean_not_equal_2 {
\find(equals(FALSE,TRUE))
\replacewith(false) 
\heuristics(concrete, simplify_boolean)
Choices: {}}
```

## boxToDiamond

```
boxToDiamond {
\find(\[{ .. #s ... }\] (post))
\replacewith(not(\<{ .. #s ... }\> (not(post)))) 
\heuristics(boxDiamondConv)
Choices: {programRules:Java}}
```

## boxToDiamond

```
boxToDiamondTransaction {
\find(box_transaction\[{ .. #s ... }\] (post))
\replacewith(not(diamond_transaction\[{ .. #s ... }\] (not(post)))) 
\heuristics(boxDiamondConv)
Choices: {programRules:Java}}
```

## box_and_left

```
box_and_left {
\find(#box ( (modal operator))\[{ .. #s ... }\] (and(post,post1))==>)
\replacewith([and(#box ( (modal operator))\[{ .. #s ... }\] (post),#box ( (modal operator))\[{ .. #s ... }\] (post1))]==>[]) 

Choices: {programRules:Java}}
```

## box_and_right

```
box_and_right {
\find(==>#box ( (modal operator))\[{ .. #s ... }\] (and(post,post1)))
\replacewith([]==>[#box ( (modal operator))\[{ .. #s ... }\] (post1)]) ;
\replacewith([]==>[#box ( (modal operator))\[{ .. #s ... }\] (post)]) 

Choices: {programRules:Java}}
```

## box_or_left

```
box_or_left {
\find(#box ( (modal operator))\[{ .. #s ... }\] (or(post,post1))==>)
\replacewith([or(#box ( (modal operator))\[{ .. #s ... }\] (post),#box ( (modal operator))\[{ .. #s ... }\] (post1))]==>[]) 

Choices: {programRules:Java}}
```

## box_or_right

```
box_or_right {
\find(==>#box ( (modal operator))\[{ .. #s ... }\] (or(post,post1)))
\replacewith([]==>[or(#box ( (modal operator))\[{ .. #s ... }\] (post),#box ( (modal operator))\[{ .. #s ... }\] (post1))]) 

Choices: {programRules:Java}}
```

## box_true

```
box_true {
\find(#box ( (modal operator))\[{ .. #s ... }\] (true))
\replacewith(true) 
\heuristics(modal_tautology)
Choices: {programRules:Java}}
```

## bprod_commutative_associative

```
bprod_commutative_associative {
\find(bprod{uSub (variable)}(i0,i2,mul(t,t2)))
\varcond(\notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)))
\replacewith(mul(bprod{uSub (variable)}(i0,i2,t),bprod{uSub1 (variable)}(i0,i2,subst{uSub (variable)}(uSub1,t2)))) 

Choices: {integerSimplificationRules:full}}
```

## bprod_empty

```
bprod_empty {
\find(bprod{uSub (variable)}(i0,i1,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(Z(1(#))) ;
\add []==>[leq(i1,i0)] 

Choices: {integerSimplificationRules:full}}
```

## bprod_equal_one_right

```
bprod_equal_one_right {
\find(==>equals(bprod{uSub (variable)}(i0,i2,t),Z(1(#))))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\add []==>[all{uSub (variable)}(subst{uSub (variable)}(uSub,imp(and(geq(uSub,i0),lt(uSub,i2)),equals(t,Z(1(#))))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## bprod_equal_zero_right

```
bprod_equal_zero_right {
\find(==>equals(bprod{uSub (variable)}(i0,i2,t),Z(0(#))))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\add []==>[exists{uSub (variable)}(subst{uSub (variable)}(uSub,and(and(geq(uSub,i0),lt(uSub,i2)),equals(t,Z(0(#))))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## bprod_find

```
bprod_find {
\find(bprod{uSub (variable)}(low,high,t))
\varcond(\notFreeIn(uSub (variable), high (int term)), \notFreeIn(uSub (variable), middle (int term)), \notFreeIn(uSub (variable), low (int term)))
\replacewith(if-then-else(and(leq(low,middle),leq(middle,high)),mul(bprod{uSub (variable)}(low,middle,t),bprod{uSub (variable)}(middle,high,t)),bprod{uSub (variable)}(low,high,t))) 
\heuristics(triggered, comprehension_split)
Choices: {integerSimplificationRules:full}}
```

## bprod_induction_lower

```
bprod_induction_lower {
\find(bprod{uSub (variable)}(i0,i2,t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(mul(bprod{uSub (variable)}(add(i0,Z(1(#))),i2,t),if-then-else(lt(i0,i2),subst{uSub (variable)}(i0,t),Z(1(#))))) 

Choices: {integerSimplificationRules:full}}
```

## bprod_induction_lower_concrete

```
bprod_induction_lower_concrete {
\find(bprod{uSub (variable)}(add(Z(neglit(1(#))),i0),i2,t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(mul(bprod{uSub (variable)}(i0,i2,t),if-then-else(lt(add(Z(neglit(1(#))),i0),i2),subst{uSub (variable)}(add(Z(neglit(1(#))),i0),t),Z(1(#))))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bprod_induction_upper

```
bprod_induction_upper {
\find(bprod{uSub (variable)}(i0,i2,t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(mul(bprod{uSub (variable)}(i0,sub(i2,Z(1(#))),t),if-then-else(lt(i0,i2),subst{uSub (variable)}(sub(i2,Z(1(#))),t),Z(1(#))))) 

Choices: {integerSimplificationRules:full}}
```

## bprod_induction_upper_concrete

```
bprod_induction_upper_concrete {
\find(bprod{uSub (variable)}(i0,add(Z(1(#)),i2),t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(mul(bprod{uSub (variable)}(i0,i2,t),if-then-else(leq(i0,i2),subst{uSub (variable)}(i2,t),Z(1(#))))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bprod_invert_index

```
bprod_invert_index {
\find(bprod{uSub (variable)}(i0,i1,t))
\varcond(\notFreeIn(uSub1 (variable), t (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(bprod{uSub1 (variable)}(neg(i1),neg(i0),subst{uSub (variable)}(neg(uSub1),t))) 

Choices: {integerSimplificationRules:full}}
```

## bprod_invert_index_concrete

```
bprod_invert_index_concrete {
\find(bprod{uSub (variable)}(mul(i0,Z(neglit(1(#)))),mul(i1,Z(neglit(1(#)))),t))
\varcond(\notFreeIn(uSub1 (variable), t (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(bprod{uSub1 (variable)}(i1,i0,subst{uSub (variable)}(neg(uSub1),t))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bprod_lower_equals_upper

```
bprod_lower_equals_upper {
\find(bprod{uSub (variable)}(i0,i0,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i0 (int term)))
\replacewith(Z(1(#))) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## bprod_one

```
bprod_one {
\find(bprod{uSub (variable)}(i0,i1,Z(1(#))))
\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(Z(1(#))) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## bprod_one_factor

```
bprod_one_factor {
\find(bprod{uSub (variable)}(i0,i1,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(if-then-else(equals(add(i0,Z(1(#))),i1),subst{uSub (variable)}(i0,t),bprod{uSub (variable)}(i0,i1,t))) 

Choices: {integerSimplificationRules:full}}
```

## bprod_one_factor_concrete1

```
bprod_one_factor_concrete1 {
\find(bprod{uSub (variable)}(i0,add(Z(1(#)),i0),t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i0 (int term)))
\replacewith(subst{uSub (variable)}(i0,t)) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## bprod_one_factor_concrete2

```
bprod_one_factor_concrete2 {
\find(bprod{uSub (variable)}(add(Z(neglit(1(#))),i0),i0,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i0 (int term)))
\replacewith(subst{uSub (variable)}(add(Z(neglit(1(#))),i0),t)) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## bprod_shift_index

```
bprod_shift_index {
\find(bprod{uSub (variable)}(i0,i1,t))
\varcond(\notFreeIn(uSub1 (variable), t (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(bprod{uSub1 (variable)}(Z(0(#)),sub(i1,i0),subst{uSub (variable)}(add(uSub1,i0),t))) 

Choices: {integerSimplificationRules:full}}
```

## bprod_zero

```
bprod_zero {
\find(bprod{uSub (variable)}(i0,i1,Z(0(#))))
\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(if-then-else(lt(i0,i1),Z(0(#)),Z(1(#)))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## break

```
break {
\find(#allmodal ( (modal operator))\[{ .. #lb0:break ;
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. do-break(#lb0:break ;  ) ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## bsum_add

```
bsum_add {
\find(add(bsum{uSub1 (variable)}(i0,i1,t1),bsum{uSub2 (variable)}(i2,i3,t2)))
\varcond(\notFreeIn(uSub2 (variable), t1 (int term)), \notFreeIn(uSub2 (variable), i3 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub1 (variable), i3 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)))
\replacewith(bsum{uSub1 (variable)}(if-then-else(lt(i0,i2),i0,i2),if-then-else(gt(i1,i3),i1,i3),subst{uSub2 (variable)}(uSub1,add(if-then-else(and(leq(i0,uSub1),lt(uSub1,i1)),t1,Z(0(#))),if-then-else(and(leq(i2,uSub1),lt(uSub1,i3)),t2,Z(0(#))))))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_add_concrete

```
bsum_add_concrete {
\find(add(bsum{uSub1 (variable)}(i0,i1,t1),bsum{uSub2 (variable)}(i1,i3,t2)))
\varcond(\notFreeIn(uSub2 (variable), t1 (int term)), \notFreeIn(uSub2 (variable), i3 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub1 (variable), i3 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)))
\replacewith(bsum{uSub1 (variable)}(i0,i3,subst{uSub2 (variable)}(uSub1,if-then-else(lt(uSub1,i1),t1,t2)))) ;
\add []==>[and(leq(i0,i1),leq(i1,i3))] 

Choices: {integerSimplificationRules:full}}
```

## bsum_commutative_associative

```
bsum_commutative_associative {
\find(bsum{uSub (variable)}(i0,i2,add(t,t2)))
\varcond(\notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)))
\replacewith(add(bsum{uSub (variable)}(i0,i2,t),bsum{uSub1 (variable)}(i0,i2,subst{uSub (variable)}(uSub1,t2)))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_def

```
bsum_def {
\find(bsum{uSub (variable)}(i0,i1,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(if-then-else(lt(i0,i1),add(bsum{uSub (variable)}(i0,sub(i1,Z(1(#))),t),subst{uSub (variable)}(sub(i1,Z(1(#))),t)),Z(0(#)))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_distributive

```
bsum_distributive {
\find(bsum{uSub (variable)}(i0,i2,mul(t,t1)))
\varcond(\notFreeIn(uSub (variable), t1 (int term)), \notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(mul(bsum{uSub (variable)}(i0,i2,t),t1)) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bsum_empty

```
bsum_empty {
\find(bsum{uSub (variable)}(i0,i1,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(Z(0(#))) ;
\add []==>[leq(i1,i0)] 

Choices: {integerSimplificationRules:full}}
```

## bsum_equal_split1

```
bsum_equal_split1 {
\find(==>equals(bsum{uSub1 (variable)}(i0,i1,t1),bsum{uSub2 (variable)}(i0,i2,t2)))
\varcond(\notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)))
\add []==>[and(and(leq(i0,i1),leq(i0,i2)),if-then-else(lt(i1,i2),equals(bsum{uSub1 (variable)}(i0,i1,sub(t1,subst{uSub2 (variable)}(uSub1,t2))),bsum{uSub2 (variable)}(i1,i2,t2)),equals(bsum{uSub1 (variable)}(i2,i1,t1),bsum{uSub2 (variable)}(i0,i2,sub(t2,subst{uSub1 (variable)}(uSub2,t1))))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## bsum_equal_split2

```
bsum_equal_split2 {
\assumes ([equals(bsum{uSub1 (variable)}(i0,i1,t1),i)]==>[]) 
\find(==>equals(bsum{uSub2 (variable)}(i0,i2,t2),i))
\varcond(\notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)))
\add []==>[and(and(leq(i0,i1),leq(i0,i2)),if-then-else(lt(i2,i1),equals(bsum{uSub1 (variable)}(i2,i1,t1),bsum{uSub2 (variable)}(i0,i2,sub(t2,subst{uSub1 (variable)}(uSub2,t1)))),equals(bsum{uSub1 (variable)}(i0,i1,sub(t1,subst{uSub2 (variable)}(uSub1,t2))),bsum{uSub2 (variable)}(i1,i2,t2))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## bsum_equal_split3

```
bsum_equal_split3 {
\find(==>equals(bsum{uSub1 (variable)}(i1,i0,t1),bsum{uSub2 (variable)}(i2,i0,t2)))
\varcond(\notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)))
\add []==>[and(and(leq(i1,i0),leq(i2,i0)),if-then-else(lt(i1,i2),equals(bsum{uSub1 (variable)}(i1,i2,t1),bsum{uSub2 (variable)}(i2,i0,sub(t2,subst{uSub1 (variable)}(uSub2,t1)))),equals(bsum{uSub1 (variable)}(i1,i0,sub(t1,subst{uSub2 (variable)}(uSub1,t2))),bsum{uSub2 (variable)}(i2,i1,t2))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## bsum_equal_split4

```
bsum_equal_split4 {
\assumes ([equals(bsum{uSub1 (variable)}(i1,i0,t1),i)]==>[]) 
\find(==>equals(bsum{uSub2 (variable)}(i2,i0,t2),i))
\varcond(\notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)))
\add []==>[and(and(leq(i1,i0),leq(i2,i0)),if-then-else(lt(i2,i1),equals(bsum{uSub1 (variable)}(i1,i0,sub(t1,subst{uSub2 (variable)}(uSub1,t2))),bsum{uSub2 (variable)}(i2,i1,t2)),equals(bsum{uSub1 (variable)}(i1,i2,t1),bsum{uSub2 (variable)}(i2,i0,sub(t2,subst{uSub1 (variable)}(uSub2,t1))))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## bsum_induction_lower

```
bsum_induction_lower {
\find(bsum{uSub (variable)}(i0,i2,t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(add(bsum{uSub (variable)}(add(i0,Z(1(#))),i2,t),if-then-else(lt(i0,i2),subst{uSub (variable)}(i0,t),Z(0(#))))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_induction_lower2

```
bsum_induction_lower2 {
\find(bsum{uSub (variable)}(i0,i2,t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(sub(bsum{uSub (variable)}(sub(i0,Z(1(#))),i2,t),if-then-else(lt(sub(i0,Z(1(#))),i2),subst{uSub (variable)}(sub(i0,Z(1(#))),t),Z(0(#))))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_induction_lower2_concrete

```
bsum_induction_lower2_concrete {
\find(bsum{uSub (variable)}(add(Z(1(#)),i0),i2,t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(sub(bsum{uSub (variable)}(i0,i2,t),if-then-else(lt(i0,i2),subst{uSub (variable)}(i0,t),Z(0(#))))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bsum_induction_lower_concrete

```
bsum_induction_lower_concrete {
\find(bsum{uSub (variable)}(add(Z(neglit(1(#))),i0),i2,t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(add(bsum{uSub (variable)}(i0,i2,t),if-then-else(lt(add(Z(neglit(1(#))),i0),i2),subst{uSub (variable)}(add(Z(neglit(1(#))),i0),t),Z(0(#))))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bsum_induction_upper

```
bsum_induction_upper {
\find(bsum{uSub (variable)}(i0,i2,t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(add(bsum{uSub (variable)}(i0,sub(i2,Z(1(#))),t),if-then-else(lt(i0,i2),subst{uSub (variable)}(sub(i2,Z(1(#))),t),Z(0(#))))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_induction_upper2

```
bsum_induction_upper2 {
\find(bsum{uSub (variable)}(i0,i2,t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(sub(bsum{uSub (variable)}(i0,add(i2,Z(1(#))),t),if-then-else(lt(i0,add(i2,Z(1(#)))),subst{uSub (variable)}(i2,t),Z(0(#))))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_induction_upper2_concrete

```
bsum_induction_upper2_concrete {
\find(bsum{uSub (variable)}(i0,add(Z(neglit(1(#))),i2),t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(sub(bsum{uSub (variable)}(i0,i2,t),if-then-else(lt(i0,i2),subst{uSub (variable)}(sub(i2,Z(1(#))),t),Z(0(#))))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bsum_induction_upper_concrete

```
bsum_induction_upper_concrete {
\find(bsum{uSub (variable)}(i0,add(Z(1(#)),i2),t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(add(bsum{uSub (variable)}(i0,i2,t),if-then-else(leq(i0,i2),subst{uSub (variable)}(i2,t),Z(0(#))))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bsum_induction_upper_concrete_2

```
bsum_induction_upper_concrete_2 {
\find(bsum{uSub (variable)}(Z(iz),Z(jz),t))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(add(bsum{uSub (variable)}(Z(iz),sub(Z(jz),Z(1(#))),t),if-then-else(leq(Z(iz),sub(Z(jz),Z(1(#)))),subst{uSub (variable)}(sub(Z(jz),Z(1(#))),t),Z(0(#))))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bsum_invert_index

```
bsum_invert_index {
\find(bsum{uSub (variable)}(i0,i1,t))
\varcond(\notFreeIn(uSub1 (variable), t (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(bsum{uSub1 (variable)}(neg(i1),neg(i0),subst{uSub (variable)}(neg(uSub1),t))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_invert_index_concrete

```
bsum_invert_index_concrete {
\find(bsum{uSub (variable)}(mul(i0,Z(neglit(1(#)))),mul(i1,Z(neglit(1(#)))),t))
\varcond(\notFreeIn(uSub1 (variable), t (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(bsum{uSub1 (variable)}(i1,i0,subst{uSub (variable)}(neg(uSub1),t))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bsum_lower_equals_upper

```
bsum_lower_equals_upper {
\find(bsum{uSub (variable)}(i0,i0,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i0 (int term)))
\replacewith(Z(0(#))) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## bsum_one_summand

```
bsum_one_summand {
\find(bsum{uSub (variable)}(i0,i1,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(if-then-else(equals(add(i0,Z(1(#))),i1),subst{uSub (variable)}(i0,t),bsum{uSub (variable)}(i0,i1,t))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_one_summand_concrete1

```
bsum_one_summand_concrete1 {
\find(bsum{uSub (variable)}(i0,add(Z(1(#)),i0),t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i0 (int term)))
\replacewith(subst{uSub (variable)}(i0,t)) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## bsum_one_summand_concrete2

```
bsum_one_summand_concrete2 {
\find(bsum{uSub (variable)}(add(Z(neglit(1(#))),i0),i0,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i0 (int term)))
\replacewith(subst{uSub (variable)}(add(Z(neglit(1(#))),i0),t)) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## bsum_positive1

```
bsum_positive1 {
\find(bsum{uSub (variable)}(i0,i1,if-then-else(b,Z(1(#)),Z(0(#)))))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\add [geq(bsum{uSub (variable)}(i0,i1,if-then-else(b,Z(1(#)),Z(0(#)))),Z(0(#)))]==>[] 

Choices: {integerSimplificationRules:full}}
```

## bsum_positive2

```
bsum_positive2 {
\find(bsum{uSub (variable)}(i0,i1,if-then-else(b,Z(0(#)),Z(1(#)))))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\add [geq(bsum{uSub (variable)}(i0,i1,if-then-else(b,Z(0(#)),Z(1(#)))),Z(0(#)))]==>[] 

Choices: {integerSimplificationRules:full}}
```

## bsum_same_summand

```
bsum_same_summand {
\find(bsum{uSub (variable)}(i0,i1,t))
\varcond(\notFreeIn(uSub (variable), t (int term)), \notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(if-then-else(geq(i1,i0),mul(t,sub(i1,i0)),Z(0(#)))) 
\heuristics(simplify)
Choices: {integerSimplificationRules:full}}
```

## bsum_shift_index

```
bsum_shift_index {
\find(bsum{uSub (variable)}(i0,i1,t))
\varcond(\notFreeIn(uSub1 (variable), t (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(bsum{uSub1 (variable)}(Z(0(#)),sub(i1,i0),subst{uSub (variable)}(add(uSub1,i0),t))) 

Choices: {integerSimplificationRules:full}}
```

## bsum_split

```
bsum_split {
\find(bsum{uSub (variable)}(low,high,t))
\varcond(\notFreeIn(uSub (variable), high (int term)), \notFreeIn(uSub (variable), middle (int term)), \notFreeIn(uSub (variable), low (int term)))
\replacewith(if-then-else(and(leq(low,middle),leq(middle,high)),add(bsum{uSub (variable)}(low,middle,t),bsum{uSub (variable)}(middle,high,t)),bsum{uSub (variable)}(low,high,t))) 
\heuristics(triggered, comprehension_split)
Choices: {integerSimplificationRules:full}}
```

## bsum_split_in_three

```
bsum_split_in_three {
\find(bsum{uSub (variable)}(i0,i2,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), t (int term)), \notFreeIn(uSub (variable), i1 (int term)))
\replacewith(add(add(bsum{uSub (variable)}(i0,i1,t),subst{uSub (variable)}(i1,t)),bsum{uSub1 (variable)}(add(i1,Z(1(#))),i2,subst{uSub (variable)}(uSub1,t)))) ;
\add []==>[and(leq(i0,i1),lt(i1,i2))] 

Choices: {integerSimplificationRules:full}}
```

## bsum_zero

```
bsum_zero {
\find(bsum{uSub (variable)}(i0,i1,Z(0(#))))
\varcond(\notFreeIn(uSub (variable), i1 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\replacewith(Z(0(#))) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## bsum_zero_right

```
bsum_zero_right {
\find(==>equals(bsum{uSub (variable)}(i0,i2,t),Z(0(#))))
\varcond(\notFreeIn(uSub (variable), i2 (int term)), \notFreeIn(uSub (variable), i0 (int term)))
\add []==>[all{uSub (variable)}(subst{uSub (variable)}(uSub,imp(and(geq(uSub,i0),lt(uSub,i2)),equals(t,Z(0(#))))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## case_distinction

```
case_distinction_r {
\find(==>b)
\addrules [to_false {
\find(==>b)
\replacewith([]==>[false]) 
\heuristics(simplify)
Choices: {}}] ;
\addrules [to_true {
\find(==>b)
\replacewith([]==>[true]) 
\heuristics(simplify)
Choices: {}}] 

Choices: {}}
```

## case_distinction

```
case_distinction_l {
\find(b==>)
\addrules [to_false {
\find(b==>)
\replacewith([false]==>[]) 
\heuristics(simplify)
Choices: {}}] ;
\addrules [to_true {
\find(b==>)
\replacewith([true]==>[]) 
\heuristics(simplify)
Choices: {}}] 

Choices: {}}
```

## cast

```
widening_identity_cast_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(byte)#seByte; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seByte; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## cast

```
widening_identity_cast_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(short)#seByte; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seByte; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## cast

```
widening_identity_cast_3 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(char)#seChar; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seChar; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## cast

```
widening_identity_cast_4 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(short)#seShort; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seShort; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## cast

```
widening_identity_cast_5 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(int)#seByteShortInt; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seByteShortInt; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## cast

```
widening_identity_cast_10 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(long)#seByteShortInt; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seByteShortInt; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## cast

```
widening_identity_cast_11 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(long)#seLong; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seLong; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## cast

```
widening_identity_cast_12 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(int)#seChar; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seChar; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## cast

```
widening_identity_cast_13 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(long)#seChar; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seChar; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## cast

```
compound_reference_cast_expression {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(#npit)#nse; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=(#npit)#v; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## cast

```
compound_byte_cast_expression {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(byte)#nse; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=(byte)#v; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## cast

```
compound_short_cast_expression {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(short)#nse; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=(short)#v; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## cast

```
compound_int_cast_expression {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(int)#nse; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=(int)#v; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## cast

```
compound_long_cast_expression {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(long)#nse; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=(long)#v; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## cast

```
narrowingByteCastShort {
\find(#normalassign ( (modal operator))\[{ .. #loc=(byte)#seShort; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastByte(#seShort)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
narrowingByteCastInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=(byte)#seInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastByte(#seInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
narrowingByteCastLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=(byte)#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastByte(#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
narrowingShortCastInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=(short)#seInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastShort(#seInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
narrowingShortCastLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=(short)#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastShort(#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
narrowingIntCastLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=(int)#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastInt(#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
narrowingCharCastByte {
\find(#normalassign ( (modal operator))\[{ .. #loc=(char)#seByte; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastChar(#seByte)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
narrowingCharCastShort {
\find(#normalassign ( (modal operator))\[{ .. #loc=(char)#seShort; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastChar(#seShort)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
narrowingCharCastInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=(char)#seInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastChar(#seInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
narrowingCharCastLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=(char)#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastChar(#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## cast

```
widening_identity_cast_bigint {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(\bigint)#seAny; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#seAny; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {bigint:on,programRules:Java}}
```

## cast

```
narrowingShortCastBigint {
\find(#allmodal ( (modal operator))\[{ .. #loc=(short)#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastShort(#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## cast

```
narrowingIntCastBigint {
\find(#allmodal ( (modal operator))\[{ .. #loc=(int)#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastInt(#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## cast

```
narrowingLongCastBigint {
\find(#allmodal ( (modal operator))\[{ .. #loc=(long)#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastLong(#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## cast

```
narrowingCharCastBigint {
\find(#allmodal ( (modal operator))\[{ .. #loc=(char)#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastChar(#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## cast

```
narrowingByteCastBigint {
\find(#allmodal ( (modal operator))\[{ .. #loc=(byte)#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaCastByte(#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## castAdd

```
castAdd2 {
\assumes ([equals(cs,gt)]==>[]) 
\find(gt)
\sameUpdateLevel\varcond(\strict\sub(C, G), )
\replacewith(C::cast(gt)) 

Choices: {}}
```

## castDel

```
castDel {
\find(C::cast(castedTerm))
\replacewith(castedTerm) 
\heuristics(cast_deletion, simplify)
Choices: {}}
```

## castDel

```
castDel2 {
\assumes ([equals(cs,gt)]==>[]) 
\find(C::cast(gt))
\sameUpdateLevel\replacewith(cs) 

Choices: {}}
```

## castToBoolean

```
castToBoolean {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(boolean)#exBool; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#exBool; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## castTrueImpliesOriginalTrue

```
castTrueImpliesOriginalTrue {
\assumes ([equals(boolean::select(h,o,f),TRUE)]==>[]) 
\find(==>equals(any::select(h,o,f),TRUE))
\replacewith([]==>[true]) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## castType

```
castType {
\assumes ([equals(H::instance(C::cast(s)),TRUE)]==>[]) 
\find(equals(CSub::instance(s),TRUE)==>)
\replacewith([equals(H::instance(s),TRUE)]==>[]) 
\heuristics(simplify)
Choices: {}}
```

## castType

```
castType2 {
\assumes ([]==>[equals(H::instance(C::cast(s)),TRUE)]) 
\find(equals(CSub::instance(s),TRUE)==>)
\replacewith([]==>[equals(H::instance(s),TRUE)]) 
\heuristics(simplify)
Choices: {}}
```

## castedGetAny

```
castedGetAny {
\find(beta::cast(any::seqGet(seq,idx)))
\replacewith(beta::seqGet(seq,idx)) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## charLiteral_to_int

```
charLiteral_to_int {
\find(C(iz))
\replacewith(Z(iz)) 
\heuristics(charLiteral_to_intLiteral)
Choices: {}}
```

## checkPermissionOwner_empty

```
checkPermissionOwner_empty {
\find(checkPermissionOwner(ow,depth,emptyPermissionOwnerList))
\add []==>[geq(depth,Z(0(#)))] ;
\replacewith(false) 
\heuristics(concrete)
Choices: {permissions:on}}
```

## checkPermissionOwner_nonempty

```
checkPermissionOwner_nonempty {
\find(checkPermissionOwner(ow,depth,consPermissionOwnerList(o,ol)))
\add []==>[geq(depth,Z(0(#)))] ;
\replacewith(if-then-else(equals(depth,Z(0(#))),equals(ow,o),checkPermissionOwner(ow,sub(depth,Z(1(#))),ol))) 
\heuristics(simplify_expression)
Choices: {permissions:on}}
```

## classes being initialized have been prepared

```
initialized_class_is_prepared {
\assumes ([equals(boolean::select(heap,null,alphaObj::<classInitialized>),TRUE),wellFormed(heap)]==>[]) 
\find(boolean::select(heap,null,alphaObj::<classPrepared>))
\sameUpdateLevel\replacewith(TRUE) 
\heuristics(simplify, confluence_restricted)
Choices: {programRules:Java}}
```

## close

```
close {
\assumes ([b]==>[]) 
\find(==>b)
\closegoal\heuristics(closure)
Choices: {}}
```

## close

```
closeAntec {
\assumes ([]==>[b]) 
\find(b==>)
\closegoal
Choices: {}}
```

## closeFalse

```
closeFalse {
\find(false==>)
\closegoal\heuristics(closure)
Choices: {}}
```

## closeTrue

```
closeTrue {
\find(==>true)
\closegoal\heuristics(closure)
Choices: {}}
```

## closeType

```
closeType {
\assumes ([]==>[equals(G::instance(t1),TRUE)]) 
\find(equals(GSub::instance(t1),TRUE)==>)
\closegoal\heuristics(closure)
Choices: {}}
```

## closeType

```
closeTypeSwitched {
\assumes ([equals(GSub::instance(t1),TRUE)]==>[]) 
\find(==>equals(G::instance(t1),TRUE))
\closegoal\heuristics(closure)
Choices: {}}
```

## close_by_lt_leq

```
close_by_lt_leq {
\assumes ([lt(i,j)]==>[]) 
\find(==>leq(add(i,Z(1(#))),j))
\replacewith([]==>[true]) 

Choices: {}}
```

## cnf_eqv

```
cnf_eqv {
\find(equiv(phi,psi))
\replacewith(and(or(phi,not(psi)),or(not(phi),psi))) 
\heuristics(notHumanReadable, cnf_expandIfThenElse, conjNormalForm)
Choices: {}}
```

## cnf_rightDist

```
cnf_rightDist {
\find(or(distLeft,and(distRight0,distRight1)))
\replacewith(and(or(distLeft,distRight0),or(distRight1,distLeft))) 
\heuristics(cnf_dist, conjNormalForm)
Choices: {}}
```

## collect_same_terms

```
collect_same_terms_1 {
\find(add(mul(i,j),mul(i,j)))
\replacewith(mul(Z(2(#)),mul(i,j))) 

Choices: {}}
```

## collect_same_terms

```
collect_same_terms_2 {
\find(add(add(mul(i,j),mul(i0,i1)),add(mul(i,j),mul(j0,j1))))
\replacewith(add(mul(Z(2(#)),mul(i,j)),add(mul(i0,i1),mul(j0,j1)))) 

Choices: {}}
```

## collect_same_terms

```
collect_same_terms_3 {
\find(add(add(neg(mul(i,j)),mul(i0,i1)),add(neg(mul(i,j)),mul(j0,j1))))
\replacewith(add(neg(mul(Z(2(#)),mul(i,j))),add(mul(i0,i1),mul(j0,j1)))) 

Choices: {}}
```

## commitJavaCardTransaction

```
commitJavaCardTransactionDiamond {
\find(==>diamond_transaction\[{ .. #commitJavaCardTransaction; ... }\] (post))
\replacewith([]==>[diamond(post)]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## commitJavaCardTransaction

```
commitJavaCardTransactionBox {
\find(==>box_transaction\[{ .. #commitJavaCardTransaction; ... }\] (post))
\replacewith([]==>[box(post)]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## commitJavaCardTransactionAPI

```
commitJavaCardTransactionAPI {
\find(==>#allmodal ( (modal operator))\[{ .. 
  #jcsystemType.#commitTransaction()@#jcsystemType; ... }\] (post))
\replacewith([]==>[#allmodal ( (modal operator))\[{ .. #commitJavaCardTransaction; ... }\] (post)]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## commuteDisjoint

```
commuteDisjoint {
\find(disjoint(commLeft,commRight))
\replacewith(disjoint(commRight,commLeft)) 
\heuristics(cnf_setComm)
Choices: {programRules:Java}}
```

## commuteIntersection

```
commuteIntersection {
\find(intersect(commLeft,commRight))
\replacewith(intersect(commRight,commLeft)) 
\heuristics(cnf_setComm)
Choices: {programRules:Java}}
```

## commuteIntersection_2

```
commuteIntersection_2 {
\find(intersect(intersect(s,commLeft),commRight))
\replacewith(intersect(intersect(s,commRight),commLeft)) 
\heuristics(cnf_setComm)
Choices: {programRules:Java}}
```

## commuteUnion

```
commuteUnion {
\find(union(commLeft,commRight))
\replacewith(union(commRight,commLeft)) 
\heuristics(cnf_setComm)
Choices: {programRules:Java}}
```

## commuteUnion_2

```
commuteUnion_2 {
\find(union(union(s,commLeft),commRight))
\replacewith(union(union(s,commRight),commLeft)) 
\heuristics(cnf_setComm)
Choices: {programRules:Java}}
```

## commute_and

```
commute_and {
\find(and(commLeft,commRight))
\replacewith(and(commRight,commLeft)) 
\heuristics(cnf_andComm, conjNormalForm)
Choices: {}}
```

## commute_and_2

```
commute_and_2 {
\find(and(and(commResidue,commLeft),commRight))
\replacewith(and(and(commResidue,commRight),commLeft)) 
\heuristics(cnf_andComm, conjNormalForm)
Choices: {}}
```

## commute_or

```
commute_or {
\find(or(commLeft,commRight))
\replacewith(or(commRight,commLeft)) 
\heuristics(cnf_orComm, conjNormalForm)
Choices: {}}
```

## commute_or_2

```
commute_or_2 {
\find(or(or(commResidue,commLeft),commRight))
\replacewith(or(or(commResidue,commRight),commLeft)) 
\heuristics(cnf_orComm, conjNormalForm)
Choices: {}}
```

## compound_assignment

```
compound_assignment_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=!#nseBool; ... }\] (post))
\varcond(\new(#v (program Variable), boolean))
\replacewith(#allmodal ( (modal operator))\[{ .. boolean #v = #nseBool;#lhs=!#v; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## compound_assignment

```
compound_assignment_3_nonsimple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#exBool0&&#nseBool1; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. if (!#exBool0)
    #lhs=false;
  else 
    #lhs=#nseBool1;
 ... }\] (post)) 
\heuristics(split_if, simplify_expression)
Choices: {programRules:Java}}
```

## compound_assignment

```
compound_assignment_3_mixed {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nseBool0&&#seBool1; ... }\] (post))
\varcond(\new(#v0 (program Variable), boolean))
\replacewith(#allmodal ( (modal operator))\[{ .. boolean #v0 = #nseBool0;#lhs=#v0&&#seBool1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## compound_assignment

```
compound_assignment_4_nonsimple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nseBool0&#exBool1; ... }\] (post))
\varcond(\new(#v1 (program Variable), boolean), \new(#v0 (program Variable), boolean))
\replacewith(#allmodal ( (modal operator))\[{ .. boolean #v0 = #nseBool0;boolean #v1 = #exBool1;#lhs=#v0&#v1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## compound_assignment

```
compound_assignment_5_nonsimple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#exBool0||#nseBool1; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. if (#exBool0)
    #lhs=true;
  else 
    #lhs=#nseBool1;
 ... }\] (post)) 
\heuristics(split_if, simplify_expression)
Choices: {programRules:Java}}
```

## compound_assignment

```
compound_assignment_5_mixed {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nseBool0||#seBool1; ... }\] (post))
\varcond(\new(#v0 (program Variable), boolean))
\replacewith(#allmodal ( (modal operator))\[{ .. boolean #v0 = #nseBool0;#lhs=#v0||#seBool1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## compound_assignment

```
compound_assignment_6_nonsimple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nseBool0|#exBool1; ... }\] (post))
\varcond(\new(#v1 (program Variable), boolean), \new(#v0 (program Variable), boolean))
\replacewith(#allmodal ( (modal operator))\[{ .. boolean #v0 = #nseBool0;boolean #v1 = #exBool1;#lhs=#v0|#v1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## compound_assignment

```
compound_assignment_xor_nonsimple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nseBool0^#exBool1; ... }\] (post))
\varcond(\new(#v1 (program Variable), boolean), \new(#v0 (program Variable), boolean))
\replacewith(#allmodal ( (modal operator))\[{ .. boolean #v0 = #nseBool0;boolean #v1 = #exBool1;#lhs=#v0^#v1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## compound_assignment_op_and

```
compound_assignment_op_and {
\find(#allmodal ( (modal operator))\[{ .. #v&=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v&(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_and_array

```
compound_assignment_op_and_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]&=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]&#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_and_attr

```
compound_assignment_op_and_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute&=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute&#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_div

```
compound_assignment_op_div {
\find(#allmodal ( (modal operator))\[{ .. #v/=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v/(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_div_array

```
compound_assignment_op_div_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]/=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]/#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_div_attr

```
compound_assignment_op_div_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute/=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute/#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_minus

```
compound_assignment_op_minus {
\find(#allmodal ( (modal operator))\[{ .. #v-=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v-(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_minus_array

```
compound_assignment_op_minus_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]-=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]-#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_minus_attr

```
compound_assignment_op_minus_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute-=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute-#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_mod

```
compound_assignment_op_mod {
\find(#allmodal ( (modal operator))\[{ .. #v%=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v%(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_mod_array

```
compound_assignment_op_mod_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]%=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]%#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_mod_attr

```
compound_assignment_op_mod_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute%=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute%#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_mul

```
compound_assignment_op_mul {
\find(#allmodal ( (modal operator))\[{ .. #v*=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v*(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_mul_array

```
compound_assignment_op_mul_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]*=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]*#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_mul_attr

```
compound_assignment_op_mul_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute*=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute*#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_or

```
compound_assignment_op_or {
\find(#allmodal ( (modal operator))\[{ .. #v|=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v|(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_or_array

```
compound_assignment_op_or_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]|=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]|#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_or_attr

```
compound_assignment_op_or_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute|=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute|#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_plus

```
compound_assignment_op_plus {
\find(#allmodal ( (modal operator))\[{ .. #v+=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v+(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_plus_array

```
compound_assignment_op_plus_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]+=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]+#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_plus_attr

```
compound_assignment_op_plus_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute+=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute+#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_shiftleft

```
compound_assignment_op_shiftleft {
\find(#allmodal ( (modal operator))\[{ .. #v<<=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v<<(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_shiftleft_array

```
compound_assignment_op_shiftleft_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]<<=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]<<#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_shiftleft_attr

```
compound_assignment_op_shiftleft_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute<<=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute<<#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_shiftright

```
compound_assignment_op_shiftright {
\find(#allmodal ( (modal operator))\[{ .. #v>>=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v>>(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_shiftright_array

```
compound_assignment_op_shiftright_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]>>=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]>>#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_shiftright_attr

```
compound_assignment_op_shiftright_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute>>=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute>>#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_unsigned_shiftright

```
compound_assignment_op_unsigned_shiftright {
\find(#allmodal ( (modal operator))\[{ .. #v>>>=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v>>>(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_unsigned_shiftright_array

```
compound_assignment_op_unsigned_shiftright_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]>>>=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]>>>#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_unsigned_shiftright_attr

```
compound_assignment_op_unsigned_shiftright_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute>>>=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute>>>#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_xor

```
compound_assignment_op_xor {
\find(#allmodal ( (modal operator))\[{ .. #v^=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #v=(#typeof(#v))(#v^(#e)); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_xor_array

```
compound_assignment_op_xor_array {
\find(#allmodal ( (modal operator))\[{ .. #e0[#e]^=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]^#e1); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_assignment_op_xor_attr

```
compound_assignment_op_xor_attr {
\find(#allmodal ( (modal operator))\[{ .. #e0.#attribute^=#e; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v = #e0;#v.#attribute=(#typeof(#attribute))(#v.#attribute^#e); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## compound_binary_neg

```
compound_binary_neg {
\find(#allmodal ( (modal operator))\[{ .. #lhs=~#nse; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v0 = #nse;#lhs=~#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## compound_unary_minus_eval

```
compound_unary_minus_eval {
\find(#allmodal ( (modal operator))\[{ .. #lhs=-#nse; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v0 = #nse;#lhs=-#v0; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## compound_unary_plus_assignment

```
compound_unary_plus_assignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs=+#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#e; ... }\] (post)) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## concatRepeatContraction3

```
concatRepeatContraction3 {
\find(regExConcat(repeat(rexp,nTimes),regEx(seqEmpty)))
\replacewith(repeat(rexp,nTimes)) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## concatRepeatContraction3Sym

```
concatRepeatContraction3Sym {
\find(regExConcat(regEx(seqEmpty),repeat(rexp,nTimes)))
\replacewith(repeat(rexp,nTimes)) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## concrete_and_1

```
concrete_and_1 {
\find(and(true,b))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## concrete_and_2

```
concrete_and_2 {
\find(and(false,b))
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## concrete_and_3

```
concrete_and_3 {
\find(and(b,true))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## concrete_and_4

```
concrete_and_4 {
\find(and(b,false))
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## concrete_eq_1

```
concrete_eq_1 {
\find(equiv(true,b))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## concrete_eq_2

```
concrete_eq_2 {
\find(equiv(false,b))
\replacewith(not(b)) 
\heuristics(concrete)
Choices: {}}
```

## concrete_eq_3

```
concrete_eq_3 {
\find(equiv(b,true))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## concrete_eq_4

```
concrete_eq_4 {
\find(equiv(b,false))
\replacewith(not(b)) 
\heuristics(concrete)
Choices: {}}
```

## concrete_impl_1

```
concrete_impl_1 {
\find(imp(true,b))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## concrete_impl_2

```
concrete_impl_2 {
\find(imp(false,b))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## concrete_impl_3

```
concrete_impl_3 {
\find(imp(b,false))
\replacewith(not(b)) 
\heuristics(concrete)
Choices: {}}
```

## concrete_impl_4

```
concrete_impl_4 {
\find(imp(b,true))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## concrete_not_1

```
concrete_not_1 {
\find(not(true))
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## concrete_not_2

```
concrete_not_2 {
\find(not(false))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## concrete_or_1

```
concrete_or_1 {
\find(or(true,b))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## concrete_or_2

```
concrete_or_2 {
\find(or(false,b))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## concrete_or_3

```
concrete_or_3 {
\find(or(b,true))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## concrete_or_4

```
concrete_or_4 {
\find(or(b,false))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## condition

```
condition {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e0 ?#e1 :#e2; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. if (#e0) {
    #lhs=#e1;
  }
             else  {
    #lhs=#e2;
  }
 ... }\] (post)) 
\heuristics(split_if, simplify_prog)
Choices: {programRules:Java}}
```

## condition

```
condition_not_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse ?#se1 :#se2; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v0 = #nse;#lhs=#v0 ?#se1 :#se2; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## condition

```
condition_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0 ?#se1 :#se2; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(equals(#se0,TRUE),#se1,#se2)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## contains

```
contains {
\find(clContains(seqConcat(seqSingleton(fstTextCharacter),textStringTail),searchString))
\sameUpdateLevel\add [equals(seqLen(searchString),newSym)]==>[] \replacewith(and(lt(newSym,seqLen(textStringTail)),or(equals(seqSub(seqConcat(seqSingleton(fstTextCharacter),textStringTail),Z(0(#)),newSym),searchString),clContains(textStringTail,searchString)))) 
\heuristics(stringsIntroduceNewSym, stringsContainsDefInline)
Choices: {Strings:on}}
```

## containsAxiomAntec

```
containsAxiomAntec {
\find(clContains(textString,searchString)==>)
\varcond(\notFreeIn(iv (variable), textString (Seq term)), \notFreeIn(iv (variable), searchString (Seq term)))
\replacewith([exists{iv (variable)}(and(and(geq(iv,Z(0(#))),leq(add(iv,seqLen(searchString)),seqLen(textString))),equals(seqSub(textString,iv,add(iv,seqLen(searchString))),searchString)))]==>[]) 
\heuristics(stringsExpandDefNormalOp)
Choices: {Strings:on}}
```

## containsAxiomSucc

```
containsAxiomSucc {
\find(==>clContains(textString,searchString))
\varcond(\notFreeIn(iv (variable), textString (Seq term)), \notFreeIn(iv (variable), searchString (Seq term)))
\replacewith([]==>[exists{iv (variable)}(and(and(geq(iv,Z(0(#))),leq(add(iv,seqLen(searchString)),seqLen(textString))),equals(seqSub(textString,iv,add(iv,seqLen(searchString))),searchString)))]) 
\heuristics(stringsExpandDefNormalOp)
Choices: {Strings:on}}
```

## createdInHeapToElementOf

```
createdInHeapToElementOf {
\find(createdInHeap(s,h))
\varcond(\notFreeIn(fv (variable), h (Heap term)), \notFreeIn(fv (variable), s (LocSet term)), \notFreeIn(ov (variable), h (Heap term)), \notFreeIn(ov (variable), s (LocSet term)))
\replacewith(all{ov (variable)}(all{fv (variable)}(imp(elementOf(ov,fv,s),or(equals(ov,null),equals(boolean::select(h,ov,java.lang.Object::<created>),TRUE)))))) 
\heuristics(classAxiom)
Choices: {programRules:Java}}
```

## createdInHeapWithAllFields

```
createdInHeapWithAllFields {
\find(createdInHeap(allFields(o),h))
\replacewith(or(equals(o,null),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## createdInHeapWithAllFieldsEQ

```
createdInHeapWithAllFieldsEQ {
\assumes ([equals(allFields(o),EQ)]==>[]) 
\find(createdInHeap(EQ,h))
\sameUpdateLevel\replacewith(or(equals(o,null),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## createdInHeapWithArrayRange

```
createdInHeapWithArrayRange {
\find(createdInHeap(arrayRange(o,lower,upper),h))
\replacewith(or(or(equals(o,null),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE)),lt(upper,lower))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## createdInHeapWithArrayRangeEQ

```
createdInHeapWithArrayRangeEQ {
\assumes ([equals(arrayRange(o,lower,upper),EQ)]==>[]) 
\find(createdInHeap(EQ,h))
\sameUpdateLevel\replacewith(or(or(equals(o,null),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE)),lt(upper,lower))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## createdInHeapWithEmpty

```
createdInHeapWithEmpty {
\find(createdInHeap(empty,h))
\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## createdInHeapWithObserver

```
createdInHeapWithObserver {
\find(==>createdInHeap(obs,h))
\varcond(\isObserver (obs (LocSet term), h (Heap term)), )
\replacewith([]==>[wellFormed(h)]) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## createdInHeapWithObserverEQ

```
createdInHeapWithObserverEQ {
\assumes ([equals(obs,EQ)]==>[]) 
\find(==>createdInHeap(EQ,h))
\varcond(\isObserver (obs (LocSet term), h (Heap term)), )
\replacewith([]==>[wellFormed(h)]) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## createdInHeapWithSelect

```
createdInHeapWithSelect {
\find(==>createdInHeap(LocSet::select(h,o,f),h))
\replacewith([]==>[wellFormed(h)]) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## createdInHeapWithSelectEQ

```
createdInHeapWithSelectEQ {
\assumes ([equals(LocSet::select(h,o,f),EQ)]==>[]) 
\find(==>createdInHeap(EQ,h))
\replacewith([]==>[wellFormed(h)]) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## createdInHeapWithSetMinusFreshLocs

```
createdInHeapWithSetMinusFreshLocs {
\find(createdInHeap(setMinus(s,freshLocs(h)),h))
\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## createdInHeapWithSetMinusFreshLocsEQ

```
createdInHeapWithSetMinusFreshLocsEQ {
\assumes ([equals(setMinus(s,freshLocs(h)),EQ)]==>[]) 
\find(createdInHeap(EQ,h))
\sameUpdateLevel\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## createdInHeapWithSingleton

```
createdInHeapWithSingleton {
\find(createdInHeap(singleton(o,f),h))
\replacewith(or(equals(o,null),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## createdInHeapWithSingletonEQ

```
createdInHeapWithSingletonEQ {
\assumes ([equals(singleton(o,f),EQ)]==>[]) 
\find(createdInHeap(EQ,h))
\sameUpdateLevel\replacewith(or(equals(o,null),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## createdInHeapWithUnion

```
createdInHeapWithUnion {
\find(==>createdInHeap(union(s,s2),h))
\replacewith([]==>[createdInHeap(s2,h)]) ;
\replacewith([]==>[createdInHeap(s,h)]) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## createdInHeapWithUnionEQ

```
createdInHeapWithUnionEQ {
\assumes ([equals(union(s,s2),EQ)]==>[]) 
\find(==>createdInHeap(EQ,h))
\replacewith([]==>[createdInHeap(s2,h)]) ;
\replacewith([]==>[createdInHeap(s,h)]) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## createdOnHeapImpliesCreatedOnPermissions

```
createdOnHeapImpliesCreatedOnPermissions {
\assumes ([wellFormed(h),wellFormed(p),permissionsFor(p,h),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE)]==>[]) 
\find(boolean::select(p,o,java.lang.Object::<created>))
\sameUpdateLevel\replacewith(TRUE) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## crossInst

```
crossInst {
\assumes ([]==>[or(or(leq(k,Z(neglit(1(#)))),geq(k,i)),c)]) 
\find(all{v (variable)}(or(or(or(leq(v,Z(neglit(1(#)))),geq(v,j)),b),a))==>)
\add [and(equals(sk,k),subst{v (variable)}(sk,or(or(or(leq(v,Z(neglit(1(#)))),geq(v,j)),b),a)))]==>[] 
\heuristics(loopInvariant)
Choices: {}}
```

## cut

```
cut {
\add []==>[cutFormula] ;
\add [cutFormula]==>[] 
\heuristics(cut)
Choices: {}}
```

## cutUpperBound

```
cutUpperBound {
\assumes ([all{v (variable)}(or(or(or(leq(v,Z(neglit(1(#)))),geq(v,j)),b),a))]==>[]) 
\find(==>or(or(leq(k,Z(neglit(1(#)))),geq(k,i)),c))
\add [not(equals(k,i))]==>[] ;
\add [equals(k,i)]==>[] 
\heuristics(loopInvariant)
Choices: {}}
```

## cut_direct

```
cut_direct {
\find(cutFormula)
\sameUpdateLevel\add []==>[cutFormula] \replacewith(false) ;
\add [cutFormula]==>[] \replacewith(true) 
\heuristics(cut_direct)
Choices: {}}
```

## cut_direct_l

```
cut_direct_l {
\find(b==>)
\add []==>[b] ;
\replacewith([b]==>[]) 

Choices: {}}
```

## cut_direct_r

```
cut_direct_r {
\find(==>b)
\add [b]==>[] ;
\replacewith([]==>[b]) 

Choices: {}}
```

## defInDomainImpliesCreated

```
defInDomainImpliesCreated {
\find(inDomainImpliesCreated(m))
\varcond(\notFreeIn(o (variable), m (Map term)))
\replacewith(all{o (variable)}(imp(inDomain(m,o),equals(boolean::select(heap,o,java.lang.Object::<created>),TRUE)))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## defIsFinite

```
defIsFinite {
\find(isFinite(m))
\varcond(\notFreeIn(s (variable), m (Map term)), \notFreeIn(vx (variable), m (Map term)))
\replacewith(exists{s (variable)}(all{vx (variable)}(equiv(inDomain(m,vx),exists{ix (variable)}(and(and(leq(Z(0(#)),ix),lt(ix,seqLen(s))),equals(any::seqGet(s,ix),vx))))))) 

Choices: {}}
```

## defMapEmpty

```
defMapEmpty {
\find(mapEmpty)
\replacewith(mapForeach{vy (variable)}(FALSE,mapUndef)) 

Choices: {}}
```

## defMapEquality

```
defMapEquality {
\find(equals(m0,m1))
\varcond(\notFreeIn(vy (variable), m1 (Map term)), \notFreeIn(vy (variable), m0 (Map term)))
\replacewith(all{vy (variable)}(and(equiv(inDomain(m0,vy),inDomain(m1,vy)),imp(inDomain(m0,vy),equals(mapGet(m0,vy),mapGet(m1,vy)))))) 

Choices: {}}
```

## defMapOverride

```
defMapOverride {
\find(mapOverride(m0,m1))
\varcond(\notFreeIn(vy (variable), m1 (Map term)), \notFreeIn(vy (variable), m0 (Map term)))
\replacewith(mapForeach{vy (variable)}(if-then-else(or(inDomain(m0,vy),inDomain(m1,vy)),TRUE,FALSE),if-then-else(inDomain(m1,vy),mapGet(m1,vy),mapGet(m0,vy)))) 

Choices: {}}
```

## defMapRemove

```
defMapRemove {
\find(mapRemove(m,key))
\varcond(\notFreeIn(vy (variable), key (any term)), \notFreeIn(vy (variable), m (Map term)))
\replacewith(mapForeach{vy (variable)}(if-then-else(and(inDomain(m,vy),not(equals(vy,key))),TRUE,FALSE),mapGet(m,vy))) 

Choices: {}}
```

## defMapSingleton

```
defMapSingleton {
\find(mapSingleton(xa,y))
\varcond(\notFreeIn(vy (variable), y (any term)), \notFreeIn(vy (variable), xa (alpha term)))
\replacewith(mapForeach{vy (variable)}(if-then-else(equals(vy,any::cast(xa)),TRUE,FALSE),y)) 

Choices: {}}
```

## defMapUpdate

```
defMapUpdate {
\find(mapUpdate(m,key,value))
\varcond(\notFreeIn(vy (variable), value (any term)), \notFreeIn(vy (variable), key (any term)), \notFreeIn(vy (variable), m (Map term)))
\replacewith(mapForeach{vy (variable)}(if-then-else(or(inDomain(m,vy),equals(vy,key)),TRUE,FALSE),if-then-else(equals(vy,key),value,mapGet(m,vy)))) 

Choices: {}}
```

## defOfEmpty

```
defOfEmpty {
\find(seqEmpty)
\varcond(\notFreeIn(uSub (variable), te (any term)))
\replacewith(seqDef{uSub (variable)}(Z(0(#)),Z(0(#)),te)) 

Choices: {sequences:on}}
```

## defOfSeqConcat

```
defOfSeqConcat {
\find(seqConcat(seq1,seq2))
\varcond(\notFreeIn(uSub (variable), seq2 (Seq term)), \notFreeIn(uSub (variable), seq1 (Seq term)))
\replacewith(seqDef{uSub (variable)}(Z(0(#)),add(seqLen(seq1),seqLen(seq2)),if-then-else(lt(uSub,seqLen(seq1)),any::seqGet(seq1,uSub),any::seqGet(seq2,sub(uSub,seqLen(seq1)))))) 

Choices: {sequences:on}}
```

## defOfSeqNPermInv

```
defOfSeqNPermInv {
\find(seqNPermInv(s))
\varcond(\notFreeIn(uSub (variable), s (Seq term)))
\replacewith(seqDef{uSub (variable)}(Z(0(#)),seqLen(s),seqIndexOf(s,uSub))) 

Choices: {moreSeqRules:on,sequences:on}}
```

## defOfSeqRemove

```
defOfSeqRemove {
\find(seqRemove(s,iv))
\varcond(\notFreeIn(uSub (variable), iv (int term)), \notFreeIn(uSub (variable), s (Seq term)))
\replacewith(if-then-else(or(lt(iv,Z(0(#))),leq(seqLen(s),iv)),s,seqDef{uSub (variable)}(Z(0(#)),sub(seqLen(s),Z(1(#))),if-then-else(lt(uSub,iv),any::seqGet(s,uSub),any::seqGet(s,add(uSub,Z(1(#)))))))) 

Choices: {moreSeqRules:on,sequences:on}}
```

## defOfSeqReverse

```
defOfSeqReverse {
\find(seqReverse(seq))
\varcond(\notFreeIn(uSub (variable), seq (Seq term)))
\replacewith(seqDef{uSub (variable)}(Z(0(#)),seqLen(seq),any::seqGet(seq,sub(sub(seqLen(seq),uSub),Z(1(#)))))) 

Choices: {sequences:on}}
```

## defOfSeqSingleton

```
defOfSeqSingleton {
\find(seqSingleton(x))
\varcond(\notFreeIn(uSub (variable), x (any term)))
\replacewith(seqDef{uSub (variable)}(Z(0(#)),Z(1(#)),x)) 

Choices: {sequences:on}}
```

## defOfSeqSub

```
defOfSeqSub {
\find(seqSub(seq,from,to))
\varcond(\notFreeIn(uSub (variable), to (int term)), \notFreeIn(uSub (variable), from (int term)), \notFreeIn(uSub (variable), seq (Seq term)))
\replacewith(seqDef{uSub (variable)}(from,to,any::seqGet(seq,uSub))) 

Choices: {sequences:on}}
```

## defOfSeqSwap

```
defOfSeqSwap {
\find(seqSwap(s,iv,jv))
\varcond(\notFreeIn(uSub (variable), jv (int term)), \notFreeIn(uSub (variable), iv (int term)), \notFreeIn(uSub (variable), s (Seq term)))
\replacewith(seqDef{uSub (variable)}(Z(0(#)),seqLen(s),if-then-else(not(and(and(and(leq(Z(0(#)),iv),leq(Z(0(#)),jv)),lt(iv,seqLen(s))),lt(jv,seqLen(s)))),any::seqGet(s,uSub),if-then-else(equals(uSub,iv),any::seqGet(s,jv),if-then-else(equals(uSub,jv),any::seqGet(s,iv),any::seqGet(s,uSub)))))) 

Choices: {moreSeqRules:on,sequences:on}}
```

## defSeq2Map

```
defSeq2Map {
\find(seq2map(s))
\varcond(\notFreeIn(ix (variable), s (Seq term)))
\replacewith(mapForeach{ix (variable)}(if-then-else(and(leq(Z(0(#)),ix),lt(ix,seqLen(s))),TRUE,FALSE),any::seqGet(s,ix))) 

Choices: {}}
```

## def_wellOrderLeqInt

```
def_wellOrderLeqInt {
\find(wellOrderLeqInt(intT1,intT2))
\replacewith(or(and(geq(intT1,intT2),lt(intT2,Z(0(#)))),and(leq(Z(0(#)),intT1),leq(intT1,intT2)))) 
\heuristics(simplify)
Choices: {}}
```

## definitionAllElementsOfArray

```
definitionAllElementsOfArray {
\find(allElementsOfArray(h,array,singleton(o,f)))
\varcond(\notFreeIn(j (variable), f (Field term)), \notFreeIn(j (variable), o (java.lang.Object term)), \notFreeIn(j (variable), array (java.lang.Object term)), \notFreeIn(j (variable), h (Heap term)))
\replacewith(infiniteUnion{j (variable)}(if-then-else(and(leq(Z(0(#)),j),lt(j,length(array))),singleton(java.lang.Object::select(h,array,arr(j)),f),empty))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## definitionAllElementsOfArray2

```
definitionAllElementsOfArray2 {
\find(allElementsOfArray(h,array,allFields(o)))
\varcond(\notFreeIn(j (variable), array (java.lang.Object term)), \notFreeIn(j (variable), h (Heap term)))
\replacewith(infiniteUnion{j (variable)}(if-then-else(and(leq(Z(0(#)),j),lt(j,length(array))),allFields(java.lang.Object::select(h,array,arr(j))),empty))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## definitionAllElementsOfArrayLocsets

```
definitionAllElementsOfArrayLocsets {
\find(allElementsOfArrayLocsets(h,array,singleton(o,f)))
\varcond(\notFreeIn(j (variable), f (Field term)), \notFreeIn(j (variable), o (java.lang.Object term)), \notFreeIn(j (variable), array (java.lang.Object term)), \notFreeIn(j (variable), h (Heap term)))
\replacewith(infiniteUnion{j (variable)}(if-then-else(and(leq(Z(0(#)),j),lt(j,length(array))),LocSet::select(h,java.lang.Object::select(h,array,arr(j)),f),empty))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## definitionOfNewObjectsIsomorphic

```
definitionOfNewObjectsIsomorphic {
\find(==>newObjectsIsomorphic(s1,h1,s2,h2))
\replacewith([]==>[objectsIsomorphic(s1,s1,s2,s2)]) ;
\replacewith([]==>[sameTypes(s1,s2)]) ;
\replacewith([]==>[newOnHeap(h2,s2)]) ;
\replacewith([]==>[newOnHeap(h1,s1)]) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## definitionOfNewOnHeap

```
definitionOfNewOnHeap {
\find(==>newOnHeap(h,s))
\varcond(\notFreeIn(i (variable), h (Heap term)), \notFreeIn(i (variable), s (Seq term)))
\replacewith([]==>[all{i (variable)}(imp(and(leq(Z(0(#)),i),lt(i,seqLen(s))),and(imp(equals(java.lang.Object::instance(any::seqGet(s,i)),TRUE),equals(boolean::select(h,java.lang.Object::seqGet(s,i),java.lang.Object::<created>),FALSE)),imp(equals(Seq::instance(any::seqGet(s,i)),TRUE),newOnHeap(h,Seq::seqGet(s,i))))))]) 
\heuristics(comprehensions)
Choices: {}}
```

## definitionOfObjectIsomorphic

```
definitionOfObjectIsomorphic {
\find(==>objectIsomorphic(s1,o1,s2,o2))
\varcond(\notFreeIn(i (variable), o2 (java.lang.Object term)), \notFreeIn(i (variable), o1 (java.lang.Object term)), \notFreeIn(i (variable), s2 (Seq term)), \notFreeIn(i (variable), s1 (Seq term)))
\replacewith([]==>[all{i (variable)}(imp(and(leq(Z(0(#)),i),lt(i,seqLen(s1))),and(imp(equals(java.lang.Object::instance(any::seqGet(s1,i)),TRUE),equiv(equals(java.lang.Object::seqGet(s1,i),o1),equals(java.lang.Object::seqGet(s2,i),o2))),imp(equals(Seq::instance(any::seqGet(s1,i)),TRUE),objectIsomorphic(Seq::seqGet(s1,i),o1,Seq::seqGet(s2,i),o2)))))]) 
\heuristics(comprehensions)
Choices: {}}
```

## definitionOfObjectsIsomorphic

```
definitionOfObjectsIsomorphic {
\find(==>objectsIsomorphic(s1,t1,s2,t2))
\varcond(\notFreeIn(i (variable), t2 (Seq term)), \notFreeIn(i (variable), t1 (Seq term)), \notFreeIn(i (variable), s2 (Seq term)), \notFreeIn(i (variable), s1 (Seq term)))
\replacewith([]==>[all{i (variable)}(imp(and(leq(Z(0(#)),i),lt(i,seqLen(t1))),and(imp(equals(java.lang.Object::instance(any::seqGet(t1,i)),TRUE),objectIsomorphic(s1,java.lang.Object::seqGet(t1,i),s2,java.lang.Object::seqGet(t2,i))),imp(equals(Seq::instance(any::seqGet(t1,i)),TRUE),objectsIsomorphic(s1,Seq::seqGet(t1,i),s2,Seq::seqGet(t2,i))))))]) 
\heuristics(comprehensions)
Choices: {}}
```

## definitionOfSameTypes

```
definitionOfSameTypes {
\find(==>sameTypes(s1,s2))
\varcond(\notFreeIn(i (variable), s2 (Seq term)), \notFreeIn(i (variable), s1 (Seq term)))
\replacewith([]==>[and(equals(seqLen(s1),seqLen(s2)),all{i (variable)}(imp(and(leq(Z(0(#)),i),lt(i,seqLen(s1))),and(sameType(any::seqGet(s1,i),any::seqGet(s2,i)),imp(equals(Seq::instance(any::seqGet(s1,i)),TRUE),sameTypes(Seq::seqGet(s1,i),Seq::seqGet(s2,i)))))))]) 
\heuristics(comprehensions)
Choices: {}}
```

## definitionSeqdefWorkaround

```
definitionSeqdefWorkaround {
\find(seq_def_workaround(h,lower,upper,array))
\varcond(\notFreeIn(j (variable), upper (int term)), \notFreeIn(j (variable), lower (int term)), \notFreeIn(j (variable), array (java.lang.Object term)), \notFreeIn(j (variable), h (Heap term)))
\replacewith(seqDef{j (variable)}(lower,upper,any::select(h,array,arr(j)))) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## definitionSeqdefWorkaround2

```
definitionSeqdefWorkaround2 {
\find(seq_def_workaround2(h,lower,upper,array,singleton(o,f)))
\varcond(\notFreeIn(j (variable), upper (int term)), \notFreeIn(j (variable), lower (int term)), \notFreeIn(j (variable), array (java.lang.Object term)), \notFreeIn(j (variable), f (Field term)), \notFreeIn(j (variable), h (Heap term)))
\replacewith(seqDef{j (variable)}(lower,upper,any::select(h,java.lang.Object::select(h,array,arr(j)),f))) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## delete_unnecessary_cast

```
delete_unnecessary_cast {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(#npit)#se; ... }\] (post))
\sameUpdateLevel\varcond(\hasSort(#npit (program NonPrimitiveType), G), \sub(\typeof(#se (program SimpleExpression)), G), )
\add []==>[or(equals(#se,null),equals(G::instance(#se),TRUE))] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.ClassCastException (); ... }\] (post)) ;
\add [or(equals(#se,null),equals(G::instance(#se),TRUE))]==>[] \replacewith(update-application(elem-update(#lhs (program LeftHandSide))(#addCast(#se,#lhs)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## diamondToBox

```
diamondToBox {
\find(\<{ .. #s ... }\> (post))
\replacewith(not(\[{ .. #s ... }\] (not(post)))) 
\heuristics(boxDiamondConv)
Choices: {programRules:Java}}
```

## diamondToBox

```
diamondToBoxTransaction {
\find(diamond_transaction\[{ .. #s ... }\] (post))
\replacewith(not(box_transaction\[{ .. #s ... }\] (not(post)))) 
\heuristics(boxDiamondConv)
Choices: {programRules:Java}}
```

## diamond_and_left

```
diamond_and_left {
\find(#diamond ( (modal operator))\[{ .. #s ... }\] (and(post,post1))==>)
\replacewith([and(#diamond ( (modal operator))\[{ .. #s ... }\] (post),#diamond ( (modal operator))\[{ .. #s ... }\] (post1))]==>[]) 

Choices: {programRules:Java}}
```

## diamond_and_right

```
diamond_and_right {
\find(==>#diamond ( (modal operator))\[{ .. #s ... }\] (and(post,post1)))
\replacewith([]==>[#diamond ( (modal operator))\[{ .. #s ... }\] (post1)]) ;
\replacewith([]==>[#diamond ( (modal operator))\[{ .. #s ... }\] (post)]) 

Choices: {programRules:Java}}
```

## diamond_false

```
diamond_false {
\find(#diamond ( (modal operator))\[{ .. #s ... }\] (false))
\replacewith(false) 
\heuristics(modal_tautology)
Choices: {programRules:Java}}
```

## diamond_or_left

```
diamond_or_left {
\find(#diamond ( (modal operator))\[{ .. #s ... }\] (or(post,post1))==>)
\replacewith([or(#diamond ( (modal operator))\[{ .. #s ... }\] (post),#diamond ( (modal operator))\[{ .. #s ... }\] (post1))]==>[]) 

Choices: {programRules:Java}}
```

## diamond_or_right

```
diamond_or_right {
\find(==>#diamond ( (modal operator))\[{ .. #s ... }\] (or(post,post1)))
\replacewith([]==>[or(#diamond ( (modal operator))\[{ .. #s ... }\] (post),#diamond ( (modal operator))\[{ .. #s ... }\] (post1))]) 

Choices: {programRules:Java}}
```

## diamond_split_termination

```
diamond_split_termination {
\find(\<{ .. #s ... }\> (post))
\replacewith(and(\[{ .. #s ... }\] (post),\<{ .. #s ... }\> (true))) 

Choices: {programRules:Java}}
```

## disjointAllFields

```
disjointAllFields {
\assumes ([equals(intersect(allFields(o),s),empty)]==>[]) 
\find(elementOf(o,f,s))
\sameUpdateLevel\replacewith(false) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## disjointAllFields_2

```
disjointAllFields_2 {
\find(equals(intersect(allFields(o),allFields(o2)),empty))
\replacewith(not(equals(o,o2))) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## disjointAllObjects

```
disjointAllObjects {
\assumes ([equals(intersect(allObjects(f),s),empty)]==>[]) 
\find(elementOf(o,f,s))
\sameUpdateLevel\replacewith(false) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## disjointAndSubset1

```
disjointAndSubset1 {
\assumes ([equals(intersect(s2,s3),empty)]==>[]) 
\find(subset(s,s2)==>)
\add [equals(intersect(s,s3),empty)]==>[] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## disjointAndSubset2

```
disjointAndSubset2 {
\assumes ([equals(intersect(s2,s3),empty)]==>[]) 
\find(subset(s,s3)==>)
\add [equals(intersect(s,s2),empty)]==>[] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## disjointAndSubset_3

```
disjointAndSubset_3 {
\assumes ([equals(intersect(s1,s2),empty)]==>[]) 
\find(subset(s4,union(s2,s3))==>)
\add [imp(equals(intersect(s1,s3),empty),equals(intersect(s1,s4),empty))]==>[] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## disjointAndSubset_4

```
disjointAndSubset_4 {
\assumes ([equals(intersect(s1,s2),empty)]==>[]) 
\find(subset(s4,union(s3,s2))==>)
\add [imp(equals(intersect(s1,s3),empty),equals(intersect(s1,s4),empty))]==>[] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## disjointAndSubset_5

```
disjointAndSubset_5 {
\assumes ([equals(intersect(s2,s1),empty)]==>[]) 
\find(subset(s4,union(s2,s3))==>)
\add [imp(equals(intersect(s1,s3),empty),equals(intersect(s1,s4),empty))]==>[] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## disjointAndSubset_6

```
disjointAndSubset_6 {
\assumes ([equals(intersect(s2,s1),empty)]==>[]) 
\find(subset(s4,union(s3,s2))==>)
\add [imp(equals(intersect(s1,s3),empty),equals(intersect(s1,s4),empty))]==>[] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## disjointDefinition

```
disjointDefinition {
\find(disjoint(s,s2))
\replacewith(equals(intersect(s,s2),empty)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## disjointInfiniteUnion

```
disjointInfiniteUnion {
\find(equals(intersect(infiniteUnion{iv (variable)}(s2),s),empty))
\varcond(\notFreeIn(iv (variable), s (LocSet term)))
\replacewith(all{iv (variable)}(equals(intersect(s2,s),empty))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## disjointInfiniteUnion_2

```
disjointInfiniteUnion_2 {
\find(equals(intersect(s,infiniteUnion{iv (variable)}(s2)),empty))
\varcond(\notFreeIn(iv (variable), s (LocSet term)))
\replacewith(all{iv (variable)}(equals(intersect(s,s2),empty))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## disjointNotInOtherLocset1

```
disjointNotInOtherLocset1 {
\assumes ([equals(intersect(s,s2),empty)]==>[]) 
\find(elementOf(o,f,s)==>)
\add []==>[elementOf(o,f,s2)] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## disjointNotInOtherLocset2

```
disjointNotInOtherLocset2 {
\assumes ([equals(intersect(s,s2),empty)]==>[]) 
\find(elementOf(o,f,s2)==>)
\add []==>[elementOf(o,f,s)] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## disjointToElementOf

```
disjointToElementOf {
\find(disjoint(s,s2))
\varcond(\notFreeIn(fv (variable), s2 (LocSet term)), \notFreeIn(fv (variable), s (LocSet term)), \notFreeIn(ov (variable), s2 (LocSet term)), \notFreeIn(ov (variable), s (LocSet term)))
\replacewith(all{ov (variable)}(all{fv (variable)}(or(not(elementOf(ov,fv,s)),not(elementOf(ov,fv,s2)))))) 
\heuristics(semantics_blasting)
Choices: {programRules:Java}}
```

## disjointWithEmpty

```
disjointWithEmpty {
\find(disjoint(empty,s))
\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## disjointWithSingleton

```
disjointWithSingleton1 {
\find(equals(intersect(s,singleton(o,f)),empty))
\replacewith(not(elementOf(o,f,s))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## disjointWithSingleton

```
disjointWithSingleton2 {
\find(equals(intersect(singleton(o,f),s),empty))
\replacewith(not(elementOf(o,f,s))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## dismissNonSelectedField

```
dismissNonSelectedField {
\find(alpha::select(store(h,o,f1,x),u,f2))
\varcond(\differentFields (f1 (Field term), f2 (Field term)), )
\replacewith(alpha::select(h,u,f2)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## dismissNonSelectedFieldEQ

```
dismissNonSelectedFieldEQ {
\assumes ([equals(store(h,o,f1,x),EQ)]==>[]) 
\find(alpha::select(EQ,u,f2))
\sameUpdateLevel\varcond(\differentFields (f1 (Field term), f2 (Field term)), )
\replacewith(alpha::select(h,u,f2)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## distr_elim

```
concrete_or_5 {
\find(or(and(c,b),and(c,not(b))))
\replacewith(c) 
\heuristics(concrete)
Choices: {}}
```

## distr_existsAnd1

```
distr_existsAnd1 {
\find(exists{u (variable)}(and(phi,psi)))
\varcond(\notFreeIn(u (variable), psi (formula)))
\replacewith(and(exists{u (variable)}(phi),psi)) 
\heuristics(notHumanReadable, distrQuantifier)
Choices: {}}
```

## distr_existsAnd2

```
distr_existsAnd2 {
\find(exists{u (variable)}(and(phi,psi)))
\varcond(\notFreeIn(u (variable), phi (formula)))
\replacewith(and(phi,exists{u (variable)}(psi))) 
\heuristics(notHumanReadable, distrQuantifier)
Choices: {}}
```

## distr_existsOr

```
distr_existsOr {
\find(exists{u (variable)}(or(phi,psi)))
\replacewith(or(exists{u (variable)}(phi),exists{u (variable)}(psi))) 
\heuristics(notHumanReadable, distrQuantifier)
Choices: {}}
```

## distr_forallAnd

```
distr_forallAnd {
\find(all{u (variable)}(and(phi,psi)))
\replacewith(and(all{u (variable)}(phi),all{u (variable)}(psi))) 
\heuristics(notHumanReadable, distrQuantifier)
Choices: {}}
```

## distr_forallOr1

```
distr_forallOr1 {
\find(all{u (variable)}(or(phi,psi)))
\varcond(\notFreeIn(u (variable), psi (formula)))
\replacewith(or(all{u (variable)}(phi),psi)) 
\heuristics(notHumanReadable, distrQuantifier)
Choices: {}}
```

## distr_forallOr2

```
distr_forallOr2 {
\find(all{u (variable)}(or(phi,psi)))
\varcond(\notFreeIn(u (variable), phi (formula)))
\replacewith(or(phi,all{u (variable)}(psi))) 
\heuristics(notHumanReadable, distrQuantifier)
Choices: {}}
```

## distributeIntersection

```
distributeIntersection {
\find(intersect(s1,union(s2,s3)))
\replacewith(union(intersect(s1,s2),intersect(s1,s3))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## distributeIntersection_2

```
distributeIntersection_2 {
\find(intersect(union(s2,s3),s1))
\replacewith(union(intersect(s2,s1),intersect(s3,s1))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## div_axiom

```
div_axiom {
\find(div(divNum,divDenom))
\sameUpdateLevel\add [or(equals(divDenom,Z(0(#))),and(and(equals(div(divNum,divDenom),quotient),leq(mul(quotient,divDenom),divNum)),if-then-else(geq(divDenom,Z(0(#))),geq(mul(quotient,divDenom),add(add(Z(1(#)),divNum),mul(Z(neglit(1(#))),divDenom))),geq(mul(quotient,divDenom),add(add(Z(1(#)),divNum),divDenom)))))]==>[] 
\heuristics(notHumanReadable, polySimp_newSmallSym, defOps_div)
Choices: {integerSimplificationRules:full}}
```

## div_literals

```
div_literals {
\find(div(Z(iz),Z(jz)))
\replacewith(#div(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## divide_eq0

```
divide_eq0 {
\assumes ([geq(divX,divXBoundPos)]==>[]) 
\find(equals(divProd,divProdBoundNonNeg)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(geq(divXBoundPos,Z(1(#))),imp(geq(divProdBoundNonNeg,Z(0(#))),leq(divY,div(divProdBoundNonNeg,divXBoundPos)))))]==>[] 
\heuristics(inEqSimp_nonLin_divide, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_eq1

```
divide_eq1 {
\assumes ([geq(divX,divXBoundNonNeg)]==>[]) 
\find(equals(divProd,divProdBoundNeg)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(geq(divXBoundNonNeg,Z(0(#))),imp(leq(divProdBoundNeg,Z(neglit(1(#)))),leq(divY,Z(neglit(1(#)))))))]==>[] 
\heuristics(inEqSimp_nonLin_neg, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_eq2

```
divide_eq2 {
\assumes ([geq(divX,divXBoundPos)]==>[]) 
\find(equals(divProd,divProdBoundNonPos)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(geq(divXBoundPos,Z(1(#))),imp(leq(divProdBoundNonPos,Z(0(#))),geq(divY,div(sub(add(divProdBoundNonPos,divXBoundPos),Z(1(#))),divXBoundPos)))))]==>[] 
\heuristics(inEqSimp_nonLin_divide, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_eq3

```
divide_eq3 {
\assumes ([geq(divX,divXBoundNonNeg)]==>[]) 
\find(equals(divProd,divProdBoundPos)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(geq(divXBoundNonNeg,Z(0(#))),imp(geq(divProdBoundPos,Z(1(#))),geq(divY,Z(1(#))))))]==>[] 
\heuristics(inEqSimp_nonLin_pos, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_eq4

```
divide_eq4 {
\assumes ([leq(divX,divXBoundNeg)]==>[]) 
\find(equals(divProd,divProdBoundNonPos)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(leq(divXBoundNeg,Z(neglit(1(#)))),imp(leq(divProdBoundNonPos,Z(0(#))),leq(divY,div(divProdBoundNonPos,divXBoundNeg)))))]==>[] 
\heuristics(inEqSimp_nonLin_divide, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_eq5

```
divide_eq5 {
\assumes ([leq(divX,divXBoundNonPos)]==>[]) 
\find(equals(divProd,divProdBoundPos)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(leq(divXBoundNonPos,Z(0(#))),imp(geq(divProdBoundPos,Z(1(#))),leq(divY,Z(neglit(1(#)))))))]==>[] 
\heuristics(inEqSimp_nonLin_neg, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_eq6

```
divide_eq6 {
\assumes ([leq(divX,divXBoundNeg)]==>[]) 
\find(equals(divProd,divProdBoundNonNeg)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(leq(divXBoundNeg,Z(neglit(1(#)))),imp(geq(divProdBoundNonNeg,Z(0(#))),geq(divY,div(divProdBoundNonNeg,divXBoundNeg)))))]==>[] 
\heuristics(inEqSimp_nonLin_divide, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_eq7

```
divide_eq7 {
\assumes ([leq(divX,divXBoundNonPos)]==>[]) 
\find(equals(divProd,divProdBoundNeg)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(leq(divXBoundNonPos,Z(0(#))),imp(leq(divProdBoundNeg,Z(neglit(1(#)))),geq(divY,Z(1(#))))))]==>[] 
\heuristics(inEqSimp_nonLin_pos, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_equation

```
divide_equation {
\find(equals(elimGcdLeft,elimGcdRight))
\replacewith(if-then-else(and(and(geq(elimGcd,Z(1(#))),equals(mod(elimGcdLeft,elimGcd),Z(0(#)))),leq(mod(elimGcdRight,elimGcd),add(Z(neglit(1(#))),elimGcd))),and(equals(mod(elimGcdRight,elimGcd),Z(0(#))),equals(div(elimGcdLeft,elimGcd),div(elimGcdRight,elimGcd))),equals(elimGcdLeft,elimGcdRight))) 

Choices: {integerSimplificationRules:full}}
```

## divide_geq

```
divide_geq {
\find(geq(elimGcdLeft,elimGcdRight))
\replacewith(if-then-else(and(and(geq(elimGcd,Z(1(#))),equals(mod(elimGcdLeft,elimGcd),Z(0(#)))),leq(mod(elimGcdRight,elimGcd),add(Z(neglit(1(#))),elimGcd))),geq(div(elimGcdLeft,elimGcd),add(Z(1(#)),div(add(Z(neglit(1(#))),elimGcdRight),elimGcd))),geq(elimGcdLeft,elimGcdRight))) 

Choices: {integerSimplificationRules:full}}
```

## divide_inEq0

```
divide_inEq0 {
\assumes ([geq(divX,divXBoundPos)]==>[]) 
\find(leq(divProd,divProdBoundNonNeg)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(geq(divXBoundPos,Z(1(#))),imp(geq(divProdBoundNonNeg,Z(0(#))),leq(divY,div(divProdBoundNonNeg,divXBoundPos)))))]==>[] 
\heuristics(inEqSimp_nonLin_divide, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_inEq1

```
divide_inEq1 {
\assumes ([geq(divX,divXBoundNonNeg)]==>[]) 
\find(leq(divProd,divProdBoundNeg)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(geq(divXBoundNonNeg,Z(0(#))),imp(leq(divProdBoundNeg,Z(neglit(1(#)))),leq(divY,Z(neglit(1(#)))))))]==>[] 
\heuristics(inEqSimp_nonLin_neg, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_inEq2

```
divide_inEq2 {
\assumes ([geq(divX,divXBoundPos)]==>[]) 
\find(geq(divProd,divProdBoundNonPos)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(geq(divXBoundPos,Z(1(#))),imp(leq(divProdBoundNonPos,Z(0(#))),geq(divY,div(sub(add(divProdBoundNonPos,divXBoundPos),Z(1(#))),divXBoundPos)))))]==>[] 
\heuristics(inEqSimp_nonLin_divide, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_inEq3

```
divide_inEq3 {
\assumes ([geq(divX,divXBoundNonNeg)]==>[]) 
\find(geq(divProd,divProdBoundPos)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(geq(divXBoundNonNeg,Z(0(#))),imp(geq(divProdBoundPos,Z(1(#))),geq(divY,Z(1(#))))))]==>[] 
\heuristics(inEqSimp_nonLin_pos, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_inEq4

```
divide_inEq4 {
\assumes ([leq(divX,divXBoundNeg)]==>[]) 
\find(geq(divProd,divProdBoundNonPos)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(leq(divXBoundNeg,Z(neglit(1(#)))),imp(leq(divProdBoundNonPos,Z(0(#))),leq(divY,div(divProdBoundNonPos,divXBoundNeg)))))]==>[] 
\heuristics(inEqSimp_nonLin_divide, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_inEq5

```
divide_inEq5 {
\assumes ([leq(divX,divXBoundNonPos)]==>[]) 
\find(geq(divProd,divProdBoundPos)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(leq(divXBoundNonPos,Z(0(#))),imp(geq(divProdBoundPos,Z(1(#))),leq(divY,Z(neglit(1(#)))))))]==>[] 
\heuristics(inEqSimp_nonLin_neg, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_inEq6

```
divide_inEq6 {
\assumes ([leq(divX,divXBoundNeg)]==>[]) 
\find(leq(divProd,divProdBoundNonNeg)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(leq(divXBoundNeg,Z(neglit(1(#)))),imp(geq(divProdBoundNonNeg,Z(0(#))),geq(divY,div(divProdBoundNonNeg,divXBoundNeg)))))]==>[] 
\heuristics(inEqSimp_nonLin_divide, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_inEq7

```
divide_inEq7 {
\assumes ([leq(divX,divXBoundNonPos)]==>[]) 
\find(leq(divProd,divProdBoundNeg)==>)
\add [imp(equals(divProd,mul(divX,divY)),imp(leq(divXBoundNonPos,Z(0(#))),imp(leq(divProdBoundNeg,Z(neglit(1(#)))),geq(divY,Z(1(#))))))]==>[] 
\heuristics(inEqSimp_nonLin_pos, inEqSimp_special_nonLin)
Choices: {integerSimplificationRules:full}}
```

## divide_leq

```
divide_leq {
\find(leq(elimGcdLeft,elimGcdRight))
\replacewith(if-then-else(and(and(geq(elimGcd,Z(1(#))),equals(mod(elimGcdLeft,elimGcd),Z(0(#)))),leq(mod(elimGcdRight,elimGcd),add(Z(neglit(1(#))),elimGcd))),leq(div(elimGcdLeft,elimGcd),div(elimGcdRight,elimGcd)),leq(elimGcdLeft,elimGcdRight))) 

Choices: {integerSimplificationRules:full}}
```

## division

```
compound_division_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse/#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v/#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## division

```
compound_division_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e/#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0/#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## division

```
assignmentDivisionInt {
\find(==>#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0/#seCharByteShortInt1; ... }\] (post))
\replacewith([]==>[not(equals(#seCharByteShortInt1,Z(0(#))))]) ;
\replacewith([]==>[update-application(elem-update(#loc (program Variable))(javaDivInt(#seCharByteShortInt0,#seCharByteShortInt1)),#normalassign(post))]) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## division

```
assignmentDivisionLong {
\find(==>#normalassign ( (modal operator))\[{ .. #loc=#se/#seLong; ... }\] (post))
\replacewith([]==>[not(equals(#seLong,Z(0(#))))]) ;
\replacewith([]==>[update-application(elem-update(#loc (program Variable))(javaDivLong(#se,#seLong)),#normalassign(post))]) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## division

```
assignmentDivisionLong2 {
\find(==>#normalassign ( (modal operator))\[{ .. #loc=#seLong/#seCharByteShortInt; ... }\] (post))
\replacewith([]==>[not(equals(#seCharByteShortInt,Z(0(#))))]) ;
\replacewith([]==>[update-application(elem-update(#loc (program Variable))(javaDivLong(#seLong,#seCharByteShortInt)),#normalassign(post))]) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## division

```
assignmentDivisionInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0/#seCharByteShortInt1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaDivInt(#seCharByteShortInt0,#seCharByteShortInt1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## division

```
assignmentDivisionLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#se/#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaDivLong(#se,#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## division

```
assignmentDivisionLong2 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong/#seCharByteShortInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaDivLong(#seLong,#seCharByteShortInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## division

```
assignmentDivisionInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0/#seCharByteShortInt1; ... }\] (post))
\replacewith(if-then-else(not(equals(#seCharByteShortInt1,Z(0(#)))),update-application(elem-update(#loc (program Variable))(javaDivInt(#seCharByteShortInt0,#seCharByteShortInt1)),#normalassign(post)),#normalassign ( (modal operator))\[{ .. throw new java.lang.ArithmeticException (); ... }\] (post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## division

```
assignmentDivisionLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#se/#seLong; ... }\] (post))
\replacewith(if-then-else(not(equals(#seLong,Z(0(#)))),update-application(elem-update(#loc (program Variable))(javaDivLong(#se,#seLong)),#normalassign(post)),#normalassign ( (modal operator))\[{ .. throw new java.lang.ArithmeticException (); ... }\] (post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## division

```
assignmentDivisionLong2 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong/#seCharByteShortInt; ... }\] (post))
\replacewith(if-then-else(not(equals(#seCharByteShortInt,Z(0(#)))),update-application(elem-update(#loc (program Variable))(javaDivLong(#seLong,#seCharByteShortInt)),#normalassign(post)),#normalassign ( (modal operator))\[{ .. throw new java.lang.ArithmeticException (); ... }\] (post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## division

```
assignmentDivisionBigint1 {
\find(==>#allmodal ( (modal operator))\[{ .. #loc=#seBigint/#seAny; ... }\] (post))
\replacewith([]==>[not(equals(#seAny,Z(0(#))))]) ;
\replacewith([]==>[update-application(elem-update(#loc (program Variable))(div(#seBigint,#seAny)),#allmodal(post))]) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ban,bigint:on,programRules:Java}}
```

## division

```
assignmentDivisionBigint2 {
\find(==>#allmodal ( (modal operator))\[{ .. #loc=#seAny/#seBigint; ... }\] (post))
\replacewith([]==>[not(equals(#seBigint,Z(0(#))))]) ;
\replacewith([]==>[update-application(elem-update(#loc (program Variable))(div(#seAny,#seBigint)),#allmodal(post))]) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ban,bigint:on,programRules:Java}}
```

## division

```
assignmentDivisionBigint1 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seBigint/#seAny; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(div(#seBigint,#seAny)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ignore,bigint:on,programRules:Java}}
```

## division

```
assignmentDivisionBigint2 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seAny/#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(div(#seAny,#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ignore,bigint:on,programRules:Java}}
```

## division

```
assignmentDivisionBigint1 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seBigint/#seAny; ... }\] (post))
\replacewith(if-then-else(not(equals(#seAny,Z(0(#)))),update-application(elem-update(#loc (program Variable))(div(#seBigint,#seAny)),#allmodal(post)),#allmodal ( (modal operator))\[{ .. throw new java.lang.ArithmeticException (); ... }\] (post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:allow,bigint:on,programRules:Java}}
```

## division

```
assignmentDivisionBigint2 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seAny/#seBigint; ... }\] (post))
\replacewith(if-then-else(not(equals(#seBigint,Z(0(#)))),update-application(elem-update(#loc (program Variable))(div(#seAny,#seBigint)),#allmodal(post)),#allmodal ( (modal operator))\[{ .. throw new java.lang.ArithmeticException (); ... }\] (post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:allow,bigint:on,programRules:Java}}
```

## doWhileUnwind

```
doWhileUnwind {
\find(#allmodal ( (modal operator))\[{ .. do#swhile ( #e );
 ... }\] (post))
\varcond(\newLabel (#innerLabel (program Label)), \newLabel (#outerLabel (program Label)), )
\replacewith(#allmodal ( (modal operator))\[{ .. #unwind-loop(do#swhile ( #e );  ) ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## doubleImpLeft

```
doubleImpLeft {
\find(imp(b,imp(c,d))==>)
\replacewith([d]==>[]) ;
\replacewith([]==>[c]) ;
\replacewith([]==>[b]) 
\heuristics(beta)
Choices: {}}
```

## double_not

```
double_not {
\find(not(not(b)))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## double_unary_minus

```
double_unary_minus_literal {
\find(Z(neglit(neglit(iz))))
\replacewith(Z(iz)) 
\heuristics(simplify_literals)
Choices: {}}
```

## dropEffectlessStores

```
dropEffectlessStores {
\find(store(h,o,f,x))
\varcond(\dropEffectlessStores(h (Heap term), o (java.lang.Object term), f (Field term), x (any term), result (Heap term)), )
\replacewith(result) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## elementOfAllFields

```
elementOfAllFields {
\find(elementOf(o,f,allFields(o2)))
\replacewith(equals(o,o2)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## elementOfAllLocs

```
elementOfAllLocs {
\find(elementOf(o,f,allLocs))
\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## elementOfAllObjects

```
elementOfAllObjects {
\find(elementOf(o,f,allObjects(f2)))
\replacewith(equals(f,f2)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## elementOfArrayRange

```
elementOfArrayRange {
\find(elementOf(o,f,arrayRange(o2,lower,upper)))
\varcond(\notFreeIn(iv (variable), upper (int term)), \notFreeIn(iv (variable), lower (int term)), \notFreeIn(iv (variable), f (Field term)))
\replacewith(and(equals(o,o2),exists{iv (variable)}(and(and(equals(f,arr(iv)),leq(lower,iv)),leq(iv,upper))))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elementOfArrayRangeConcrete

```
elementOfArrayRangeConcrete {
\find(elementOf(o,arr(idx),arrayRange(o2,lower,upper)))
\replacewith(and(and(equals(o,o2),leq(lower,idx)),leq(idx,upper))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## elementOfArrayRangeEQ

```
elementOfArrayRangeEQ {
\assumes ([equals(arrayRange(o2,lower,upper),EQ)]==>[]) 
\find(elementOf(o,f,EQ))
\sameUpdateLevel\varcond(\notFreeIn(iv (variable), upper (int term)), \notFreeIn(iv (variable), lower (int term)), \notFreeIn(iv (variable), f (Field term)))
\replacewith(and(equals(o,o2),exists{iv (variable)}(and(and(equals(f,arr(iv)),leq(lower,iv)),leq(iv,upper))))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## elementOfEmpty

```
elementOfEmpty {
\find(elementOf(o,f,empty))
\replacewith(false) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## elementOfFreshLocs

```
elementOfFreshLocs {
\find(elementOf(o,f,freshLocs(h)))
\replacewith(and(not(equals(o,null)),not(equals(boolean::select(h,o,java.lang.Object::<created>),TRUE)))) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## elementOfGuardedSet

```
elementOfGuardedSet {
\find(elementOf(o,f,if-then-else(phi,s,empty)))
\replacewith(and(phi,elementOf(o,f,s))) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## elementOfInfiniteUnion

```
elementOfInfiniteUnion {
\find(elementOf(o,f,infiniteUnion{av (variable)}(s)))
\varcond(\notFreeIn(av (variable), f (Field term)), \notFreeIn(av (variable), o (java.lang.Object term)))
\replacewith(exists{av (variable)}(elementOf(o,f,s))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## elementOfInfiniteUnion2Vars

```
elementOfInfiniteUnion2Vars {
\find(elementOf(o,f,infiniteUnion{av (variable), bv (variable)}(s)))
\varcond(\notFreeIn(bv (variable), f (Field term)), \notFreeIn(bv (variable), o (java.lang.Object term)), \notFreeIn(av (variable), f (Field term)), \notFreeIn(av (variable), o (java.lang.Object term)))
\replacewith(exists{av (variable)}(exists{bv (variable)}(elementOf(o,f,s)))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## elementOfInfiniteUnion2VarsEQ

```
elementOfInfiniteUnion2VarsEQ {
\assumes ([equals(infiniteUnion{av (variable), bv (variable)}(s),EQ)]==>[]) 
\find(elementOf(o,f,EQ))
\sameUpdateLevel\varcond(\notFreeIn(bv (variable), f (Field term)), \notFreeIn(bv (variable), o (java.lang.Object term)), \notFreeIn(av (variable), f (Field term)), \notFreeIn(av (variable), o (java.lang.Object term)))
\replacewith(exists{av (variable)}(exists{bv (variable)}(elementOf(o,f,s)))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## elementOfInfiniteUnionEQ

```
elementOfInfiniteUnionEQ {
\assumes ([equals(infiniteUnion{av (variable)}(s),EQ)]==>[]) 
\find(elementOf(o,f,EQ))
\sameUpdateLevel\varcond(\notFreeIn(av (variable), f (Field term)), \notFreeIn(av (variable), o (java.lang.Object term)))
\replacewith(exists{av (variable)}(elementOf(o,f,s))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## elementOfIntersect

```
elementOfIntersect {
\find(elementOf(o,f,intersect(s,s2)))
\replacewith(and(elementOf(o,f,s),elementOf(o,f,s2))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elementOfIntersectEQ

```
elementOfIntersectEQ {
\assumes ([equals(intersect(s,s2),EQ)]==>[]) 
\find(elementOf(o,f,EQ))
\sameUpdateLevel\replacewith(and(elementOf(o,f,s),elementOf(o,f,s2))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elementOfSetMinus

```
elementOfSetMinus {
\find(elementOf(o,f,setMinus(s,s2)))
\replacewith(and(elementOf(o,f,s),not(elementOf(o,f,s2)))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elementOfSetMinusEQ

```
elementOfSetMinusEQ {
\assumes ([equals(setMinus(s,s2),EQ)]==>[]) 
\find(elementOf(o,f,EQ))
\sameUpdateLevel\replacewith(and(elementOf(o,f,s),not(elementOf(o,f,s2)))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elementOfSingleton

```
elementOfSingleton {
\find(elementOf(o,f,singleton(o2,f2)))
\replacewith(and(equals(o,o2),equals(f,f2))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## elementOfSubsetImpliesElementOfSuperset

```
elementOfSubsetImpliesElementOfSuperset {
\assumes ([subset(s,s2)]==>[]) 
\find(elementOf(o,f,s)==>)
\add [elementOf(o,f,s2)]==>[] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elementOfSubsetOfUnion1

```
elementOfSubsetOfUnion1 {
\assumes ([subset(s,union(s2,s3))]==>[elementOf(o,f,s2)]) 
\find(elementOf(o,f,s))
\sameUpdateLevel\add [equiv(elementOf(o,f,s),elementOf(o,f,intersect(s,s3)))]==>[] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elementOfSubsetOfUnion2

```
elementOfSubsetOfUnion2 {
\assumes ([subset(s,union(s2,s3))]==>[elementOf(o,f,s3)]) 
\find(elementOf(o,f,s))
\sameUpdateLevel\add [equiv(elementOf(o,f,s),elementOf(o,f,intersect(s,s2)))]==>[] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elementOfUnion

```
elementOfUnion {
\find(elementOf(o,f,union(s,s2)))
\replacewith(or(elementOf(o,f,s),elementOf(o,f,s2))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elementOfUnionEQ

```
elementOfUnionEQ {
\assumes ([equals(union(s,s2),EQ)]==>[]) 
\find(elementOf(o,f,EQ))
\sameUpdateLevel\replacewith(or(elementOf(o,f,s),elementOf(o,f,s2))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## elimGcdEq

```
elimGcdEq {
\find(equals(elimGcdLeft,elimGcdRight))
\replacewith(if-then-else(and(and(equals(mul(elimGcdLeftDiv,elimGcd),elimGcdLeft),leq(add(elimGcdRight,mul(mul(elimGcd,Z(neglit(1(#)))),elimGcdRightDiv)),add(Z(neglit(1(#))),elimGcd))),geq(add(elimGcdRight,mul(mul(elimGcd,Z(neglit(1(#)))),elimGcdRightDiv)),Z(0(#)))),and(equals(add(elimGcdRight,mul(mul(elimGcd,Z(neglit(1(#)))),elimGcdRightDiv)),Z(0(#))),equals(elimGcdLeftDiv,elimGcdRightDiv)),equals(elimGcdLeft,elimGcdRight))) 
\heuristics(notHumanReadable, polySimp_pullOutGcd)
Choices: {integerSimplificationRules:full}}
```

## elimGcdGeq

```
elimGcdGeq {
\find(geq(elimGcdLeft,elimGcdRight))
\replacewith(if-then-else(and(geq(add(add(add(add(sub(elimGcd,Z(1(#))),mul(mul(elimGcd,Z(neglit(1(#)))),elimGcdRightDiv)),elimGcdRight),mul(elimGcdLeftDiv,elimGcd)),mul(elimGcdLeft,Z(neglit(1(#))))),Z(0(#))),leq(add(add(add(mul(mul(elimGcd,Z(neglit(1(#)))),elimGcdRightDiv),elimGcdRight),mul(elimGcdLeftDiv,elimGcd)),mul(elimGcdLeft,Z(neglit(1(#))))),Z(0(#)))),geq(elimGcdLeftDiv,elimGcdRightDiv),geq(elimGcdLeft,elimGcdRight))) 
\heuristics(notHumanReadable, inEqSimp_pullOutGcd_geq, inEqSimp_pullOutGcd)
Choices: {integerSimplificationRules:full}}
```

## elimGcdGeq_antec

```
elimGcdGeq_antec {
\find(geq(elimGcdLeft,elimGcdRight)==>)
\replacewith([or(or(leq(elimGcd,Z(0(#))),leq(add(add(add(add(elimGcd,mul(mul(elimGcd,Z(neglit(1(#)))),elimGcdRightDiv)),elimGcdRight),mul(elimGcdLeftDiv,elimGcd)),mul(elimGcdLeft,Z(neglit(1(#))))),Z(0(#)))),geq(elimGcdLeftDiv,elimGcdRightDiv))]==>[]) 
\heuristics(notHumanReadable, inEqSimp_pullOutGcd_antec, inEqSimp_pullOutGcd_geq, inEqSimp_pullOutGcd)
Choices: {integerSimplificationRules:full}}
```

## elimGcdLeq

```
elimGcdLeq {
\find(leq(elimGcdLeft,elimGcdRight))
\replacewith(if-then-else(and(leq(add(add(add(add(sub(Z(1(#)),elimGcd),mul(mul(elimGcd,Z(neglit(1(#)))),elimGcdRightDiv)),elimGcdRight),mul(elimGcdLeftDiv,elimGcd)),mul(elimGcdLeft,Z(neglit(1(#))))),Z(0(#))),geq(add(add(add(mul(mul(elimGcd,Z(neglit(1(#)))),elimGcdRightDiv),elimGcdRight),mul(elimGcdLeftDiv,elimGcd)),mul(elimGcdLeft,Z(neglit(1(#))))),Z(0(#)))),leq(elimGcdLeftDiv,elimGcdRightDiv),leq(elimGcdLeft,elimGcdRight))) 
\heuristics(notHumanReadable, inEqSimp_pullOutGcd_leq, inEqSimp_pullOutGcd)
Choices: {integerSimplificationRules:full}}
```

## elimGcdLeq_antec

```
elimGcdLeq_antec {
\find(leq(elimGcdLeft,elimGcdRight)==>)
\replacewith([or(or(leq(elimGcd,Z(0(#))),geq(add(add(add(add(neg(elimGcd),mul(mul(elimGcd,Z(neglit(1(#)))),elimGcdRightDiv)),elimGcdRight),mul(elimGcdLeftDiv,elimGcd)),mul(elimGcdLeft,Z(neglit(1(#))))),Z(0(#)))),leq(elimGcdLeftDiv,elimGcdRightDiv))]==>[]) 
\heuristics(notHumanReadable, inEqSimp_pullOutGcd_antec, inEqSimp_pullOutGcd_leq, inEqSimp_pullOutGcd)
Choices: {integerSimplificationRules:full}}
```

## elim_double_block

```
elim_double_block {
\find(#allmodal ( (modal operator))\[{ {#slist}}\] (post))
\replacewith(#allmodal ( (modal operator))\[{#slist}\] (post)) 

Choices: {programRules:Java}}
```

## elim_double_block

```
elim_double_block_2 {
\find(#allmodal ( (modal operator))\[{ ..  { {#slist}} ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {#slist} ... }\] (post)) 

Choices: {programRules:Java}}
```

## elim_double_block

```
elim_double_block_3 {
\find(#allmodal ( (modal operator))\[{ ..  {while ( #e )
      #s
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. while ( #e )
    #s
 ... }\] (post)) 

Choices: {programRules:Java}}
```

## elim_double_block

```
elim_double_block_4 {
\find(#allmodal ( (modal operator))\[{ ..  {for ( #loopInit; #guard; #forupdates )
      #s
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. for ( #loopInit; #guard; #forupdates )
    #s
 ... }\] (post)) 

Choices: {programRules:Java}}
```

## elim_double_block

```
elim_double_block_5 {
\find(#allmodal ( (modal operator))\[{ ..  {for ( ; #guard; #forupdates )
      #s
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. for ( ; #guard; #forupdates )
    #s
 ... }\] (post)) 

Choices: {programRules:Java}}
```

## elim_double_block

```
elim_double_block_6 {
\find(#allmodal ( (modal operator))\[{ ..  {for ( #loopInit; #guard;  )
      #s
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. for ( #loopInit; #guard;  )
    #s
 ... }\] (post)) 

Choices: {programRules:Java}}
```

## elim_double_block

```
elim_double_block_7 {
\find(#allmodal ( (modal operator))\[{ ..  {for ( ; #guard;  )
      #s
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. for ( ; #guard;  )
    #s
 ... }\] (post)) 

Choices: {programRules:Java}}
```

## elim_double_block

```
elim_double_block_8 {
\find(#allmodal ( (modal operator))\[{ ..  {do#swhile ( #e );
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. do#swhile ( #e );
 ... }\] (post)) 

Choices: {programRules:Java}}
```

## elim_exists0

```
elim_exists0 {
\find(exists{Gvar (variable)}(equals(Gvar,subGterm)))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(true) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists1

```
elim_exists1 {
\find(exists{Gvar (variable)}(equals(subGterm,Gvar)))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(true) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists2

```
elim_exists2 {
\find(exists{Gvar (variable)}(equals(Gvar,Hterm)))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(equals(G::instance(Hterm),TRUE)) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_exists3

```
elim_exists3 {
\find(exists{Gvar (variable)}(equals(Hterm,Gvar)))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(equals(G::instance(Hterm),TRUE)) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_exists4

```
elim_exists4 {
\find(exists{Gvar (variable)}(and(phi,equals(Gvar,subGterm))))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,phi)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists5

```
elim_exists5 {
\find(exists{Gvar (variable)}(and(phi,equals(subGterm,Gvar))))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,phi)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists6

```
elim_exists6 {
\find(exists{Gvar (variable)}(and(phi,equals(Gvar,Hterm))))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(and(subst{Gvar (variable)}(G::cast(Hterm),phi),equals(G::instance(Hterm),TRUE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_exists7

```
elim_exists7 {
\find(exists{Gvar (variable)}(and(phi,equals(Hterm,Gvar))))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(and(subst{Gvar (variable)}(G::cast(Hterm),phi),equals(G::instance(Hterm),TRUE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_exists_leq

```
elim_exists_leq {
\find(exists{INTVar (variable)}(and(leq(INTVar,intTermLeft),geq(INTVar,intTermRight))))
\varcond(\notFreeIn(INTVar (variable), intTermRight (int term)), \notFreeIn(INTVar (variable), intTermLeft (int term)))
\replacewith(leq(intTermRight,intTermLeft)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists_nonSingleton0

```
elim_exists_nonSingleton0 {
\find(exists{nonSingleVar (variable)}(not(equals(nonSingleVar,Hterm))))
\varcond(\notFreeIn(nonSingleVar (variable), Hterm (H term)))
\replacewith(true) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists_nonSingleton1

```
elim_exists_nonSingleton1 {
\find(exists{nonSingleVar (variable)}(not(equals(Hterm,nonSingleVar))))
\varcond(\notFreeIn(nonSingleVar (variable), Hterm (H term)))
\replacewith(true) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists_nonSingleton2

```
elim_exists_nonSingleton2 {
\find(exists{INTVar (variable)}(geq(INTVar,intTerm)))
\varcond(\notFreeIn(INTVar (variable), intTerm (int term)))
\replacewith(true) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists_nonSingleton3

```
elim_exists_nonSingleton3 {
\find(exists{INTVar (variable)}(leq(INTVar,intTerm)))
\varcond(\notFreeIn(INTVar (variable), intTerm (int term)))
\replacewith(true) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists_nonSingleton4

```
elim_exists_nonSingleton4 {
\find(exists{INTVar (variable)}(geq(intTerm,INTVar)))
\varcond(\notFreeIn(INTVar (variable), intTerm (int term)))
\replacewith(true) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists_nonSingleton5

```
elim_exists_nonSingleton5 {
\find(exists{INTVar (variable)}(leq(intTerm,INTVar)))
\varcond(\notFreeIn(INTVar (variable), intTerm (int term)))
\replacewith(true) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists_sub_1

```
elim_exists_sub_1 {
\find(exists{locVar (variable)}(and(subset(locVar,locSetTermRight),subset(locSetTermLeft,locVar))))
\varcond(\notFreeIn(locVar (variable), locSetTermRight (LocSet term)), \notFreeIn(locVar (variable), locSetTermLeft (LocSet term)))
\replacewith(subset(locSetTermLeft,locSetTermRight)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists_sub_1_and_phi

```
elim_exists_sub_1_and_phi {
\find(exists{locVar (variable)}(and(and(subset(locVar,locSetTerm),subset(locSetTerm,locVar)),phi)))
\varcond(\notFreeIn(locVar (variable), locSetTerm (LocSet term)))
\replacewith(exists{locVar (variable)}(and(equals(locVar,locSetTerm),phi))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_exists_sub_1_or_phi

```
elim_exists_sub_1_or_phi {
\find(exists{locVar (variable)}(or(and(subset(locVar,locSetTermRight),subset(locSetTermLeft,locVar)),phi)))
\varcond(\notFreeIn(locVar (variable), locSetTermRight (LocSet term)), \notFreeIn(locVar (variable), locSetTermLeft (LocSet term)))
\replacewith(or(subset(locSetTermLeft,locSetTermRight),exists{locVar (variable)}(phi))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall0

```
elim_forall0 {
\find(all{Gvar (variable)}(not(equals(Gvar,subGterm))))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(false) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall1

```
elim_forall1 {
\find(all{Gvar (variable)}(not(equals(subGterm,Gvar))))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(false) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall10

```
elim_forall10 {
\find(all{Gvar (variable)}(imp(equals(Gvar,Hterm),phi)))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(or(subst{Gvar (variable)}(G::cast(Hterm),phi),equals(G::instance(Hterm),FALSE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall11

```
elim_forall11 {
\find(all{Gvar (variable)}(imp(equals(Hterm,Gvar),phi)))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(or(subst{Gvar (variable)}(G::cast(Hterm),phi),equals(G::instance(Hterm),FALSE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall12

```
elim_forall12 {
\find(all{Gvar (variable)}(imp(and(psi,equals(Gvar,subGterm)),phi)))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,imp(psi,phi))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall13

```
elim_forall13 {
\find(all{Gvar (variable)}(imp(and(psi,equals(subGterm,Gvar)),phi)))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,imp(psi,phi))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall14

```
elim_forall14 {
\find(all{Gvar (variable)}(imp(and(psi,equals(Gvar,Hterm)),phi)))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(or(subst{Gvar (variable)}(G::cast(Hterm),imp(psi,phi)),equals(G::instance(Hterm),FALSE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall15

```
elim_forall15 {
\find(all{Gvar (variable)}(imp(and(psi,equals(Hterm,Gvar)),phi)))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(or(subst{Gvar (variable)}(G::cast(Hterm),imp(psi,phi)),equals(G::instance(Hterm),FALSE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall16

```
elim_forall16 {
\find(all{Gvar (variable)}(imp(and(equals(Gvar,subGterm),psi),phi)))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,imp(psi,phi))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall17

```
elim_forall17 {
\find(all{Gvar (variable)}(imp(and(equals(subGterm,Gvar),psi),phi)))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,imp(psi,phi))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall18

```
elim_forall18 {
\find(all{Gvar (variable)}(imp(and(equals(Gvar,Hterm),psi),phi)))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(or(subst{Gvar (variable)}(G::cast(Hterm),imp(psi,phi)),equals(G::instance(Hterm),FALSE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall19

```
elim_forall19 {
\find(all{Gvar (variable)}(imp(and(equals(Hterm,Gvar),psi),phi)))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(or(subst{Gvar (variable)}(G::cast(Hterm),imp(psi,phi)),equals(G::instance(Hterm),FALSE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall2

```
elim_forall2 {
\find(all{Gvar (variable)}(not(equals(Gvar,Hterm))))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(equals(G::instance(Hterm),FALSE)) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall3

```
elim_forall3 {
\find(all{Gvar (variable)}(not(equals(Hterm,Gvar))))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(equals(G::instance(Hterm),FALSE)) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall4

```
elim_forall4 {
\find(all{Gvar (variable)}(or(phi,not(equals(Gvar,subGterm)))))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,phi)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall5

```
elim_forall5 {
\find(all{Gvar (variable)}(or(phi,not(equals(subGterm,Gvar)))))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,phi)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall6

```
elim_forall6 {
\find(all{Gvar (variable)}(or(phi,not(equals(Gvar,Hterm)))))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(or(subst{Gvar (variable)}(G::cast(Hterm),phi),equals(G::instance(Hterm),FALSE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall7

```
elim_forall7 {
\find(all{Gvar (variable)}(or(phi,not(equals(Hterm,Gvar)))))
\varcond(\notFreeIn(Gvar (variable), Hterm (H term)))
\replacewith(or(subst{Gvar (variable)}(G::cast(Hterm),phi),equals(G::instance(Hterm),FALSE))) 
\heuristics(elimQuantifierWithCast, elimQuantifier)
Choices: {}}
```

## elim_forall8

```
elim_forall8 {
\find(all{Gvar (variable)}(imp(equals(Gvar,subGterm),phi)))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,phi)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall9

```
elim_forall9 {
\find(all{Gvar (variable)}(imp(equals(subGterm,Gvar),phi)))
\varcond(\notFreeIn(Gvar (variable), subGterm (subG term)))
\replacewith(subst{Gvar (variable)}(subGterm,phi)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_eqSet_imp_phi

```
elim_forall_eqSet_imp_phi {
\find(all{locVar (variable)}(imp(and(subset(locVar,locSetTerm),subset(locSetTerm,locVar)),phi)))
\varcond(\notFreeIn(locVar (variable), locSetTerm (LocSet term)))
\replacewith(all{locVar (variable)}(imp(equals(locSetTerm,locVar),phi))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_leq

```
elim_forall_leq {
\find(all{INTVar (variable)}(or(leq(INTVar,intTermLeft),geq(INTVar,intTermRight))))
\varcond(\notFreeIn(INTVar (variable), intTermRight (int term)), \notFreeIn(INTVar (variable), intTermLeft (int term)))
\replacewith(leq(intTermRight,add(intTermLeft,Z(1(#))))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_nonSingleton0

```
elim_forall_nonSingleton0 {
\find(all{nonSingleVar (variable)}(equals(nonSingleVar,Hterm)))
\varcond(\notFreeIn(nonSingleVar (variable), Hterm (H term)))
\replacewith(false) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_nonSingleton1

```
elim_forall_nonSingleton1 {
\find(all{nonSingleVar (variable)}(equals(Hterm,nonSingleVar)))
\varcond(\notFreeIn(nonSingleVar (variable), Hterm (H term)))
\replacewith(false) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_nonSingleton2

```
elim_forall_nonSingleton2 {
\find(all{INTVar (variable)}(geq(INTVar,intTerm)))
\varcond(\notFreeIn(INTVar (variable), intTerm (int term)))
\replacewith(false) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_nonSingleton3

```
elim_forall_nonSingleton3 {
\find(all{INTVar (variable)}(leq(INTVar,intTerm)))
\varcond(\notFreeIn(INTVar (variable), intTerm (int term)))
\replacewith(false) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_nonSingleton4

```
elim_forall_nonSingleton4 {
\find(all{INTVar (variable)}(geq(intTerm,INTVar)))
\varcond(\notFreeIn(INTVar (variable), intTerm (int term)))
\replacewith(false) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_nonSingleton5

```
elim_forall_nonSingleton5 {
\find(all{INTVar (variable)}(leq(intTerm,INTVar)))
\varcond(\notFreeIn(INTVar (variable), intTerm (int term)))
\replacewith(false) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_subOfAll

```
elim_forall_subOfAll {
\find(all{locVar (variable)}(subset(locSetTerm,locVar)))
\varcond(\notFreeIn(locVar (variable), locSetTerm (LocSet term)))
\replacewith(equals(locSetTerm,empty)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_subOfAll_and_phi

```
elim_forall_subOfAll_and_phi {
\find(all{locVar (variable)}(and(subset(locSetTerm,locVar),phi)))
\varcond(\notFreeIn(locVar (variable), locSetTerm (LocSet term)))
\replacewith(and(equals(locSetTerm,empty),all{locVar (variable)}(phi))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_superOfAll

```
elim_forall_superOfAll {
\find(all{locVar (variable)}(subset(locVar,locSetTerm)))
\varcond(\notFreeIn(locVar (variable), locSetTerm (LocSet term)))
\replacewith(equals(locSetTerm,allLocs)) 
\heuristics(elimQuantifier)
Choices: {}}
```

## elim_forall_superOfAll_and_phi

```
elim_forall_superOfAll_and_phi {
\find(all{locVar (variable)}(and(subset(locVar,locSetTerm),phi)))
\varcond(\notFreeIn(locVar (variable), locSetTerm (LocSet term)))
\replacewith(and(equals(locSetTerm,allLocs),all{locVar (variable)}(phi))) 
\heuristics(elimQuantifier)
Choices: {}}
```

## emptyEqualsSingleton

```
emptyEqualsSingleton {
\find(equals(empty,singleton(o,f)))
\replacewith(false) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## emptyModality

```
emptyModality {
\find(#normal(post))
\replacewith(post) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## emptyModality

```
emptyModalityBoxTransaction {
\find(box_transaction(post))
\replacewith(true) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## emptyModality

```
emptyModalityDiamondTransaction {
\find(diamond_transaction(post))
\replacewith(false) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## emptyStatement

```
emptyStatement {
\find(#allmodal ( (modal operator))\[{ .. ; ... }\] (post))
\replacewith(#allmodal(post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## endsWith

```
endsWith {
\find(clEndsWith(sourceStr,searchStr))
\replacewith(if-then-else(gt(seqLen(searchStr),seqLen(sourceStr)),false,equals(seqSub(sourceStr,sub(seqLen(sourceStr),seqLen(searchStr)),seqLen(sourceStr)),searchStr))) 
\heuristics(defOpsStartsEndsWith)
Choices: {Strings:on}}
```

## enhancedfor

```
enhancedfor_iterable {
\find(#allmodal ( (modal operator))\[{ .. for (#ty #id : #e)
    #stm
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. enhancedfor-elim(for (#ty #id : #e)
      #stm  ) ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eqClose

```
eqClose {
\find(equals(s,s))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## eqSameSeq

```
eqSameSeq {
\find(equals(seqSub(seq,from,to),seq))
\sameUpdateLevel\varcond(\notFreeIn(iv (variable), seq (Seq term)), \notFreeIn(iv (variable), from (int term)))
\replacewith(or(or(and(equals(from,Z(0(#))),equals(seqLen(seq),to)),and(leq(to,from),equals(seqLen(seq),Z(0(#))))),and(equals(seqLen(seq),sub(to,from)),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(seq))),equals(any::seqGet(seq,iv),any::seqGet(seq,add(iv,from)))))))) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcat

```
eqSeqConcat {
\find(equals(s,seqConcat(s1,s2)))
\replacewith(and(and(equals(seqLen(s),add(seqLen(s1),seqLen(s2))),equals(seqSub(s,Z(0(#)),seqLen(s1)),s1)),equals(seqSub(s,seqLen(s1),seqLen(s)),s2))) 
\heuristics(simplify_ENLARGING)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcat2

```
eqSeqConcat2 {
\find(equals(seqConcat(s,s1),seqConcat(s,s2)))
\replacewith(equals(s1,s2)) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcat2EQ

```
eqSeqConcat2EQ {
\assumes ([equals(seqConcat(s,s2),EQ)]==>[]) 
\find(equals(seqConcat(s,s1),EQ))
\replacewith(equals(s1,s2)) 
\heuristics(simplify, no_self_application)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcat3

```
eqSeqConcat3 {
\find(equals(seqConcat(s1,s),seqConcat(s2,s)))
\replacewith(equals(s1,s2)) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcat3EQ

```
eqSeqConcat3EQ {
\assumes ([equals(seqConcat(s2,s),EQ)]==>[]) 
\find(equals(seqConcat(s1,s),EQ))
\replacewith(equals(s1,s2)) 
\heuristics(simplify, no_self_application)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcat4

```
eqSeqConcat4 {
\find(equals(seqConcat(s1,seqSingleton(x)),seqConcat(s2,seqSingleton(y))))
\replacewith(and(equals(s1,s2),equals(x,y))) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcat4EQ

```
eqSeqConcat4EQ {
\assumes ([equals(seqConcat(s2,seqSingleton(x)),EQ)]==>[]) 
\find(equals(seqConcat(s1,seqSingleton(y)),EQ))
\replacewith(and(equals(s1,s2),equals(x,y))) 
\heuristics(simplify, no_self_application)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcat5

```
eqSeqConcat5 {
\find(equals(seqConcat(seqSingleton(x),s1),seqConcat(seqSingleton(y),s2)))
\replacewith(and(equals(x,y),equals(s1,s2))) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcat5EQ

```
eqSeqConcat5EQ {
\assumes ([equals(seqConcat(seqSingleton(x),s2),EQ)]==>[]) 
\find(equals(seqConcat(seqSingleton(y),s1),EQ))
\replacewith(and(equals(x,y),equals(s1,s2))) 
\heuristics(simplify, no_self_application)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqConcatEQ

```
eqSeqConcatEQ {
\assumes ([equals(seqConcat(s1,s2),EQ)]==>[]) 
\find(equals(s,EQ))
\replacewith(and(and(equals(seqLen(s),add(seqLen(s1),seqLen(s2))),equals(seqSub(s,Z(0(#)),seqLen(s1)),s1)),equals(seqSub(s,seqLen(s1),seqLen(s)),s2))) 
\heuristics(simplify_ENLARGING, no_self_application)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqDef

```
eqSeqDef {
\find(equals(s,seqDef{i (variable)}(l,u,a)))
\varcond(\notFreeIn(iv (variable), a (any term)), \notFreeIn(iv (variable), u (int term)), \notFreeIn(iv (variable), l (int term)), \notFreeIn(iv (variable), s (Seq term)))
\replacewith(and(equals(seqLen(s),seqLen(seqDef{i (variable)}(l,u,a))),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(any::seqGet(s,iv),any::seqGet(seqDef{i (variable)}(l,u,a),iv)))))) 
\heuristics(simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqDef2

```
eqSeqDef2 {
\assumes ([equals(seqDef{i (variable)}(l,u,a),t)]==>[]) 
\find(equals(s,t))
\varcond(\notFreeIn(iv (variable), t (Seq term)), \notFreeIn(iv (variable), a (any term)), \notFreeIn(iv (variable), u (int term)), \notFreeIn(iv (variable), l (int term)), \notFreeIn(iv (variable), s (Seq term)))
\replacewith(and(equals(seqLen(s),seqLen(seqDef{i (variable)}(l,u,a))),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(any::seqGet(s,iv),any::seqGet(seqDef{i (variable)}(l,u,a),iv)))))) 
\heuristics(simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqEmpty

```
eqSeqEmpty {
\find(equals(s,seqEmpty))
\replacewith(equals(seqLen(s),Z(0(#)))) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqReverse

```
eqSeqReverse {
\find(equals(s,seqReverse(s2)))
\varcond(\notFreeIn(iv (variable), s2 (Seq term)), \notFreeIn(iv (variable), s (Seq term)))
\replacewith(and(equals(seqLen(s),seqLen(seqReverse(s2))),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(any::seqGet(s,iv),any::seqGet(seqReverse(s2),iv)))))) 
\heuristics(simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqReverse2

```
eqSeqReverse2 {
\assumes ([equals(seqReverse(s2),t)]==>[]) 
\find(equals(s,t))
\varcond(\notFreeIn(iv (variable), t (Seq term)), \notFreeIn(iv (variable), s2 (Seq term)), \notFreeIn(iv (variable), s (Seq term)))
\replacewith(and(equals(seqLen(s),seqLen(seqReverse(s2))),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(any::seqGet(s,iv),any::seqGet(seqReverse(s2),iv)))))) 
\heuristics(no_self_application, simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqSingleton

```
eqSeqSingleton {
\find(equals(s,seqSingleton(x)))
\replacewith(and(equals(seqLen(s),Z(1(#))),equals(any::seqGet(s,Z(0(#))),x))) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSeqSingleton2

```
eqSeqSingleton2 {
\assumes ([equals(seqSingleton(x),s2)]==>[]) 
\find(equals(s,s2))
\replacewith(and(equals(seqLen(s),Z(1(#))),equals(any::seqGet(s,Z(0(#))),x))) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## eqSymm

```
eqSymm {
\find(equals(commEqLeft,commEqRight))
\replacewith(equals(commEqRight,commEqLeft)) 
\heuristics(order_terms)
Choices: {}}
```

## eqTermCut

```
eqTermCut {
\find(t)
\sameUpdateLevel\add [not(equals(t,s))]==>[] ;
\add [equals(t,s)]==>[] 

Choices: {}}
```

## eq_add_iff1

```
eq_add_iff1 {
\find(equals(add(mul(i0,i1),i2),add(mul(i3,i1),i4)))
\replacewith(equals(add(mul(sub(i0,i3),i1),i2),i4)) 

Choices: {}}
```

## eq_add_iff2

```
eq_add_iff2 {
\find(equals(add(mul(i0,i1),i2),add(mul(i3,i1),i4)))
\replacewith(equals(i2,add(mul(sub(i3,i0),i1),i4))) 

Choices: {}}
```

## eq_and

```
eq_and {
\find(and(phi,phi))
\replacewith(phi) 
\heuristics(concrete)
Choices: {}}
```

## eq_and_2

```
eq_and_2 {
\find(and(and(psi,phi),phi))
\replacewith(and(psi,phi)) 
\heuristics(concrete)
Choices: {}}
```

## eq_eq

```
eq_eq {
\find(equiv(phi,phi))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## eq_imp

```
eq_imp {
\find(imp(phi,phi))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## eq_or

```
eq_or {
\find(or(phi,phi))
\replacewith(phi) 
\heuristics(concrete)
Choices: {}}
```

## eq_or_2

```
eq_or_2 {
\find(or(or(psi,phi),phi))
\replacewith(or(psi,phi)) 
\heuristics(concrete)
Choices: {}}
```

## eq_sides

```
eq_sides {
\find(equals(i,j))
\replacewith(equals(sub(i,j),Z(0(#)))) 

Choices: {}}
```

## equalCharacters

```
equalCharacters {
\find(equals(C(iz1),C(iz2)))
\replacewith(equals(Z(iz1),Z(iz2))) 
\heuristics(stringsSimplify)
Choices: {Strings:on}}
```

## equalRegEx

```
equalRegEx {
\find(equals(rexp1,rexp2))
\varcond(\notFreeIn(string (variable), rexp2 (RegEx term)), \notFreeIn(string (variable), rexp1 (RegEx term)))
\replacewith(all{string (variable)}(equiv(match(rexp1,string),match(rexp2,string)))) 

Choices: {Strings:on}}
```

## equalUnique

```
equalUnique {
\find(equals(f,f2))
\varcond(\equalUnique (f (any term), f2 (any term), result (formula)), )
\replacewith(result) 
\heuristics(concrete)
Choices: {}}
```

## equal_add

```
equal_add {
\find(==>equals(i0,i1))
\varcond(\notFreeIn(j2 (variable), i1 (int term)), \notFreeIn(j2 (variable), i0 (int term)))
\replacewith([]==>[exists{j2 (variable)}(equals(add(i0,j2),add(i1,j2)))]) 

Choices: {}}
```

## equal_add_one

```
equal_add_one {
\find(equals(i0,i1))
\replacewith(equals(add(i0,Z(1(#))),add(i1,Z(1(#))))) 

Choices: {}}
```

## equal_bprod

```
equal_bprod5 {
\find(==>equals(bprod{uSub1 (variable)}(i0,i1,t1),bprod{uSub2 (variable)}(i2,i3,t2)))
\varcond(\notFreeIn(uSub2 (variable), i3 (int term)), \notFreeIn(uSub1 (variable), i3 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[all{uSub1 (variable)}(imp(and(geq(uSub1,i0),lt(uSub1,i1)),equals(t1,subst{uSub2 (variable)}(sub(add(uSub1,i2),i0),t2))))] ;
\add []==>[equals(sub(i1,i0),sub(i3,i2))] 
\heuristics(comprehensions_high_costs)
Choices: {integerSimplificationRules:full}}
```

## equal_bprod_perm

```
equal_bprod_perm1 {
\find(==>equals(bprod{uSub1 (variable)}(i0,i1,t1),bprod{uSub2 (variable)}(i2,i3,t2)))
\varcond(\notFreeIn(uSub2 (variable), i3 (int term)), \notFreeIn(uSub1 (variable), i3 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[seqPerm(seqDef{uSub1 (variable)}(i0,i1,t1),seqDef{uSub2 (variable)}(i2,i3,t2))] 

Choices: {sequences:on,integerSimplificationRules:full}}
```

## equal_bprod_perm

```
equal_bprod_perm2 {
\assumes ([equals(bprod{uSub2 (variable)}(i2,i3,t2),t)]==>[]) 
\find(==>equals(bprod{uSub1 (variable)}(i0,i1,t1),t))
\varcond(\notFreeIn(uSub2 (variable), i3 (int term)), \notFreeIn(uSub1 (variable), i3 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[seqPerm(seqDef{uSub1 (variable)}(i0,i1,t1),seqDef{uSub2 (variable)}(i2,i3,t2))] 

Choices: {sequences:on,integerSimplificationRules:full}}
```

## equal_bprod_same_index

```
equal_bprod1 {
\find(==>equals(bprod{uSub1 (variable)}(i0,i1,t1),bprod{uSub2 (variable)}(i0,i1,t2)))
\varcond(\notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[all{uSub1 (variable)}(imp(and(geq(uSub1,i0),lt(uSub1,i1)),equals(t1,subst{uSub2 (variable)}(uSub1,t2))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## equal_bprod_same_index

```
equal_bprod2 {
\assumes ([equals(bprod{uSub1 (variable)}(i0,i1,t1),i)]==>[]) 
\find(==>equals(bprod{uSub2 (variable)}(i0,i1,t2),i))
\varcond(\notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[all{uSub1 (variable)}(imp(and(geq(uSub1,i0),lt(uSub1,i1)),equals(t1,subst{uSub2 (variable)}(uSub1,t2))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## equal_bprod_same_index

```
equal_bprod3 {
\assumes ([equals(bprod{uSub1 (variable)}(i0,i1,t1),i),equals(bprod{uSub2 (variable)}(i0,i1,t2),j)]==>[]) 
\find(==>equals(j,i))
\varcond(\notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[all{uSub1 (variable)}(imp(and(geq(uSub1,i0),lt(uSub1,i1)),equals(t1,subst{uSub2 (variable)}(uSub1,t2))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## equal_bsum

```
equal_bsum5 {
\find(==>equals(bsum{uSub1 (variable)}(i0,i1,t1),bsum{uSub2 (variable)}(i2,i3,t2)))
\varcond(\notFreeIn(uSub2 (variable), i3 (int term)), \notFreeIn(uSub1 (variable), i3 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[all{uSub1 (variable)}(imp(and(geq(uSub1,i0),lt(uSub1,i1)),equals(t1,subst{uSub2 (variable)}(sub(add(uSub1,i2),i0),t2))))] ;
\add []==>[equals(sub(i1,i0),sub(i3,i2))] 
\heuristics(comprehensions_high_costs)
Choices: {integerSimplificationRules:full}}
```

## equal_bsum_perm

```
equal_bsum_perm1 {
\find(==>equals(bsum{uSub1 (variable)}(i0,i1,t1),bsum{uSub2 (variable)}(i2,i3,t2)))
\varcond(\notFreeIn(uSub2 (variable), i3 (int term)), \notFreeIn(uSub1 (variable), i3 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[seqPerm(seqDef{uSub1 (variable)}(i0,i1,t1),seqDef{uSub2 (variable)}(i2,i3,t2))] 

Choices: {sequences:on,integerSimplificationRules:full}}
```

## equal_bsum_perm

```
equal_bsum_perm2 {
\assumes ([equals(bsum{uSub2 (variable)}(i2,i3,t2),t)]==>[]) 
\find(==>equals(bsum{uSub1 (variable)}(i0,i1,t1),t))
\varcond(\notFreeIn(uSub2 (variable), i3 (int term)), \notFreeIn(uSub1 (variable), i3 (int term)), \notFreeIn(uSub2 (variable), i2 (int term)), \notFreeIn(uSub1 (variable), i2 (int term)), \notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[seqPerm(seqDef{uSub1 (variable)}(i0,i1,t1),seqDef{uSub2 (variable)}(i2,i3,t2))] 

Choices: {sequences:on,integerSimplificationRules:full}}
```

## equal_bsum_same_index

```
equal_bsum1 {
\find(==>equals(bsum{uSub1 (variable)}(i0,i1,t1),bsum{uSub2 (variable)}(i0,i1,t2)))
\varcond(\notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[all{uSub1 (variable)}(imp(and(geq(uSub1,i0),lt(uSub1,i1)),equals(t1,subst{uSub2 (variable)}(uSub1,t2))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## equal_bsum_same_index

```
equal_bsum2 {
\assumes ([equals(bsum{uSub1 (variable)}(i0,i1,t1),i)]==>[]) 
\find(==>equals(bsum{uSub2 (variable)}(i0,i1,t2),i))
\varcond(\notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[all{uSub1 (variable)}(imp(and(geq(uSub1,i0),lt(uSub1,i1)),equals(t1,subst{uSub2 (variable)}(uSub1,t2))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## equal_bsum_same_index

```
equal_bsum3 {
\assumes ([equals(bsum{uSub1 (variable)}(i0,i1,t1),i),equals(bsum{uSub2 (variable)}(i0,i1,t2),j)]==>[]) 
\find(==>equals(j,i))
\varcond(\notFreeIn(uSub2 (variable), i1 (int term)), \notFreeIn(uSub2 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), i1 (int term)), \notFreeIn(uSub1 (variable), i0 (int term)), \notFreeIn(uSub1 (variable), t2 (int term)), \notFreeIn(uSub2 (variable), t1 (int term)))
\add []==>[all{uSub1 (variable)}(imp(and(geq(uSub1,i0),lt(uSub1,i1)),equals(t1,subst{uSub2 (variable)}(uSub1,t2))))] 
\heuristics(comprehensions)
Choices: {integerSimplificationRules:full}}
```

## equal_bsum_zero_cut

```
equal_bsum_zero_cut {
\find(==>equals(bsum{uSub1 (variable)}(i0,i1,t1),mul(bsum{uSub2 (variable)}(i2,i3,t2),t)))
\add [equals(bsum{uSub1 (variable)}(i0,i1,t1),Z(0(#)))]==>[] ;
\add []==>[equals(bsum{uSub1 (variable)}(i0,i1,t1),Z(0(#)))] 

Choices: {integerSimplificationRules:full}}
```

## equal_literals

```
equal_literals {
\find(equals(Z(iz),Z(jz)))
\replacewith(#eq(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## equality

```
compound_equality_comparison_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse0==#se; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse0 (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse0) #v0 = #nse0;#lhs=#v0==#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## equality

```
compound_equality_comparison_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e==#nse0; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse0 (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse0) #v1 = #nse0;#lhs=#v0==#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## equality comparison

```
equality_comparison_new {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0==#se1; ... }\] (post))
\replacewith(if-then-else(not(equals(#se0,#se1)),#allmodal ( (modal operator))\[{ .. #lhs=false; ... }\] (post),#allmodal ( (modal operator))\[{ .. #lhs=true; ... }\] (post))) 
\heuristics(split_if, simplify_prog, obsolete)
Choices: {programRules:Java}}
```

## equality comparison

```
equality_comparison_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0==#se1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(equals(#se0,#se1),TRUE,FALSE)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## equalityToElementOf

```
equalityToElementOf {
\find(equals(s,s2))
\varcond(\notFreeIn(fv (variable), s2 (LocSet term)), \notFreeIn(fv (variable), s (LocSet term)), \notFreeIn(ov (variable), s2 (LocSet term)), \notFreeIn(ov (variable), s (LocSet term)))
\replacewith(all{ov (variable)}(all{fv (variable)}(equiv(elementOf(ov,fv,s),elementOf(ov,fv,s2))))) 
\heuristics(semantics_blasting)
Choices: {programRules:Java}}
```

## equalityToElementOfRight

```
equalityToElementOfRight {
\find(==>equals(s,s2))
\varcond(\notFreeIn(fv (variable), s2 (LocSet term)), \notFreeIn(fv (variable), s (LocSet term)), \notFreeIn(ov (variable), s2 (LocSet term)), \notFreeIn(ov (variable), s (LocSet term)))
\replacewith([]==>[all{ov (variable)}(all{fv (variable)}(equiv(elementOf(ov,fv,s),elementOf(ov,fv,s2))))]) 
\heuristics(setEqualityBlastingRight)
Choices: {programRules:Java}}
```

## equalityToSelect

```
equalityToSelect {
\find(equals(h,h2))
\varcond(\notFreeIn(fv (variable), h2 (Heap term)), \notFreeIn(fv (variable), h (Heap term)), \notFreeIn(ov (variable), h2 (Heap term)), \notFreeIn(ov (variable), h (Heap term)))
\replacewith(all{ov (variable)}(all{fv (variable)}(equals(any::select(h,ov,fv),any::select(h2,ov,fv))))) 
\heuristics(semantics_blasting)
Choices: {programRules:Java}}
```

## equalityToSeqGetAndSeqLen

```
equalityToSeqGetAndSeqLen {
\find(equals(left,right))
\varcond(\notFreeIn(iv (variable), right (Seq term)), \notFreeIn(iv (variable), left (Seq term)))
\replacewith(and(equals(seqLen(left),seqLen(right)),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(left))),equals(any::seqGet(left,iv),any::seqGet(right,iv)))))) 
\heuristics(defOpsSeqEquality)
Choices: {sequences:on}}
```

## equalityToSeqGetAndSeqLenLeft

```
equalityToSeqGetAndSeqLenLeft {
\find(equals(s,s2)==>)
\varcond(\notFreeIn(iv (variable), s2 (Seq term)), \notFreeIn(iv (variable), s (Seq term)))
\add [and(equals(seqLen(s),seqLen(s2)),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(any::seqGet(s,iv),any::seqGet(s2,iv)))))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {sequences:on}}
```

## equalityToSeqGetAndSeqLenRight

```
equalityToSeqGetAndSeqLenRight {
\find(==>equals(s,s2))
\varcond(\notFreeIn(iv (variable), s2 (Seq term)), \notFreeIn(iv (variable), s (Seq term)))
\replacewith([]==>[and(equals(seqLen(s),seqLen(s2)),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(any::seqGet(s,iv),any::seqGet(s2,iv)))))]) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## equiv_left

```
equiv_left {
\find(equiv(b,c)==>)
\replacewith([]==>[b,c]) ;
\replacewith([b,c]==>[]) 
\heuristics(beta)
Choices: {}}
```

## equiv_right

```
equiv_right {
\find(==>equiv(b,c))
\replacewith([c]==>[b]) ;
\replacewith([b]==>[c]) 
\heuristics(beta)
Choices: {}}
```

## erroneous classes have no initialized subclasses

```
erroneous_class_has_no_initialized_sub_class {
\assumes ([equals(boolean::select(heap,null,alphaObj::<classErroneous>),TRUE),wellFormed(heap)]==>[]) 
\find(boolean::select(heap,null,betaObj::<classInitialized>))
\sameUpdateLevel\varcond(\sub(betaObj, alphaObj), )
\replacewith(FALSE) 
\heuristics(simplify, confluence_restricted)
Choices: {programRules:Java}}
```

## eval_array_access

```
eval_array_this_access {
\find(#allmodal ( (modal operator))\[{ .. this[#nse]=#se0; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v0 = #nse;this[#v0]=#se0; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_0_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=#e1;#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_0_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=#e;#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=#e;#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_1_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]*=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]*#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_1_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute*=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute*#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1*=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1*#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_2_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]/=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]/#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_2_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute/=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute/#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1/=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1/#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_3_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]%=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]%#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_3_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute%=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute%#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_3 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1%=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1%#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_4_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]+=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]+#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_4_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute+=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute+#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_4 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1+=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1+#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_5_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]-=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]-#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_5_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute-=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute-#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_5 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1-=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1-#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_6_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]<<=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]<<#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_6_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute<<=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute<<#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_6 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1<<=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1<<#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_7_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]>>=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]>>#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_7_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute>>=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute>>#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_7 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1>>=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1>>#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_8_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]>>>=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]>>>#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_8_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute>>>=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute>>>#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_8 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1>>>=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1>>>#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_9_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]&=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]&#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_9_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute&=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute&#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_9 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1&=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1&#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_10_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]|=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]|#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_10_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute|=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute|#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_10 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1|=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1|#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_11_0 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0[#e]^=#e1; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#e (program Expression))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#typeof(#e) #v1 = #e;#v0[#v1]=(#typeof(#e0[#e]))(#v0[#v1]^#e1);#lhs0=#v0[#v1]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
eval_order_iterated_assignments_11_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e0.#attribute^=#e; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e0) #v0 = #e0;#v0.#attribute=(#typeof(#attribute))(#v0.#attribute^#e);#lhs0=#v0.#attribute; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## eval_order_iterated_assignments

```
iterated_assignments_11 {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1^=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1^#e);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## evaluate assert condition

```
evaluateAssertCondition_1 {
\find(#allmodal ( (modal operator))\[{ .. assert #nse1; ... }\] (b))
\varcond(\new(#condition (program Variable), \typeof(#nse1 (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse1) #condition = #nse1;assert #condition; ... }\] (b)) 
\heuristics(simplify_prog)
Choices: {assertions:on,programRules:Java}}
```

## evaluate assert condition

```
evaluateAssertCondition_2 {
\find(#allmodal ( (modal operator))\[{ .. assert #nse1 : #e; ... }\] (b))
\varcond(\new(#condition (program Variable), \typeof(#nse1 (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse1) #condition = #nse1;assert #condition : #e; ... }\] (b)) 
\heuristics(simplify_prog)
Choices: {assertions:on,programRules:Java}}
```

## evaluate assert condition

```
evaluateAssertMessage {
\find(#allmodal ( (modal operator))\[{ .. assert #se1 : #nse2; ... }\] (b))
\varcond(\new(#message (program Variable), \typeof(#nse2 (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse2) #message = #nse2;assert #se1 : #message; ... }\] (b)) 
\heuristics(simplify_prog)
Choices: {assertions:on,programRules:Java}}
```

## exLeft

```
exLeft {
\find(exists{u (variable)}(b)==>)
\replacewith([subst{u (variable)}(sk,b)]==>[]) 
\heuristics(delta)
Choices: {}}
```

## exRight

```
exRight {
\find(==>exists{u (variable)}(b))
\add []==>[subst{u (variable)}(t,b)] 
\heuristics(gamma)
Choices: {}}
```

## exRightHide

```
exRightHide {
\find(==>exists{u (variable)}(b))
\addrules [insert_hidden {
\add []==>[exists{u (variable)}(b)] 

Choices: {}}] \replacewith([]==>[subst{u (variable)}(t,b)]) 
\heuristics(gamma_destructive)
Choices: {}}
```

## ex_bool

```
ex_bool {
\find(exists{x (variable)}(c))
\replacewith(or(subst{x (variable)}(FALSE,c),subst{x (variable)}(TRUE,c))) 
\heuristics(boolean_cases)
Choices: {}}
```

## ex_pull_out0

```
ex_pull_out0 {
\find(and(exists{u (variable)}(b),c))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(exists{u (variable)}(and(b,c))) 
\heuristics(pullOutQuantifierEx)
Choices: {}}
```

## ex_pull_out1

```
ex_pull_out1 {
\find(and(c,exists{u (variable)}(b)))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(exists{u (variable)}(and(c,b))) 
\heuristics(pullOutQuantifierEx)
Choices: {}}
```

## ex_pull_out2

```
ex_pull_out2 {
\find(or(exists{u (variable)}(b),c))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(exists{u (variable)}(or(b,c))) 
\heuristics(pullOutQuantifierEx)
Choices: {}}
```

## ex_pull_out3

```
ex_pull_out3 {
\find(or(c,exists{u (variable)}(b)))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(exists{u (variable)}(or(c,b))) 
\heuristics(pullOutQuantifierEx)
Choices: {}}
```

## ex_pull_out4

```
ex_pull_out4 {
\find(or(exists{u (variable)}(b),exists{u2 (variable)}(c)))
\varcond(\notFreeIn(u (variable), c (formula)))
\replacewith(exists{u (variable)}(or(b,subst{u2 (variable)}(u,c)))) 
\heuristics(pullOutQuantifierUnifying, pullOutQuantifierEx)
Choices: {}}
```

## ex_unused

```
ex_unused {
\find(exists{u (variable)}(b))
\varcond(\notFreeIn(u (variable), b (formula)))
\replacewith(b) 
\heuristics(elimQuantifier)
Choices: {}}
```

## exact_instance_definition_boolean

```
exact_instance_definition_boolean {
\find(equals(boolean::exactInstance(bool),TRUE))
\varcond(\notFreeIn(bv (variable), bool (boolean term)))
\replacewith(exists{bv (variable)}(equals(bool,bv))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## exact_instance_definition_int

```
exact_instance_definition_int {
\find(equals(int::exactInstance(idx0),TRUE))
\varcond(\notFreeIn(iv (variable), idx0 (int term)))
\replacewith(exists{iv (variable)}(equals(idx0,iv))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## exact_instance_definition_null

```
exact_instance_definition_null {
\find(equals(Null::exactInstance(obj),TRUE))
\varcond(\notFreeIn(bv (variable), bool (boolean term)))
\replacewith(equals(obj,null)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## exact_instance_known_dynamic_type

```
exact_instance_known_dynamic_type {
\assumes ([equals(G::exactInstance(a),TRUE)]==>[]) 
\find(H::exactInstance(a))
\sameUpdateLevel\varcond(\not\same(G, H), )
\replacewith(FALSE) 
\heuristics(evaluate_instanceof, simplify)
Choices: {}}
```

## expand_addJint

```
expand_addJint {
\find(addJint(i,i1))
\replacewith(moduloInt(add(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_addJlong

```
expand_addJlong {
\find(addJlong(i,i1))
\replacewith(moduloLong(add(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_divJint

```
expand_divJint {
\find(divJint(i,i1))
\replacewith(moduloInt(jdiv(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_divJlong

```
expand_divJlong {
\find(divJlong(i,i1))
\replacewith(moduloLong(jdiv(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_inByte

```
expand_inByte {
\find(inByte(i))
\replacewith(and(leq(i,byte_MAX),leq(byte_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## expand_inByte

```
expand_inByte {
\find(inByte(i))
\replacewith(and(leq(i,byte_MAX),leq(byte_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## expand_inChar

```
expand_inChar {
\find(inChar(i))
\replacewith(and(leq(i,char_MAX),leq(char_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## expand_inChar

```
expand_inChar {
\find(inChar(i))
\replacewith(and(leq(i,char_MAX),leq(char_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## expand_inInt

```
expand_inInt {
\find(inInt(i))
\replacewith(and(leq(i,int_MAX),leq(int_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## expand_inInt

```
expand_inInt {
\find(inInt(i))
\replacewith(and(leq(i,int_MAX),leq(int_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## expand_inLong

```
expand_inLong {
\find(inLong(i))
\replacewith(and(leq(i,long_MAX),leq(long_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## expand_inLong

```
expand_inLong {
\find(inLong(i))
\replacewith(and(leq(i,long_MAX),leq(long_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## expand_inShort

```
expand_inShort {
\find(inShort(i))
\replacewith(and(leq(i,short_MAX),leq(short_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## expand_inShort

```
expand_inShort {
\find(inShort(i))
\replacewith(and(leq(i,short_MAX),leq(short_MIN,i))) 
\heuristics(defOps_expandRanges)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## expand_modJint

```
expand_modJint {
\find(modJint(i,i1))
\replacewith(moduloInt(jmod(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_modJlong

```
expand_modJlong {
\find(modJlong(i,i1))
\replacewith(moduloLong(jmod(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_moduloByte

```
expand_moduloByte {
\find(moduloByte(i))
\replacewith(add(byte_MIN,mod(add(byte_HALFRANGE,i),byte_RANGE))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_moduloChar

```
expand_moduloChar {
\find(moduloChar(i))
\replacewith(mod(i,add(char_MAX,Z(1(#))))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_moduloInteger

```
expand_moduloInteger {
\find(moduloInt(i))
\replacewith(add(int_MIN,mod(add(int_HALFRANGE,i),int_RANGE))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_moduloLong

```
expand_moduloLong {
\find(moduloLong(i))
\replacewith(add(long_MIN,mod(add(long_HALFRANGE,i),long_RANGE))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_moduloShort

```
expand_moduloShort {
\find(moduloShort(i))
\replacewith(add(short_MIN,mod(add(short_HALFRANGE,i),short_RANGE))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_mulJint

```
expand_mulJint {
\find(mulJint(i,i1))
\replacewith(moduloInt(mul(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_mulJlong

```
expand_mulJlong {
\find(mulJlong(i,i1))
\replacewith(moduloLong(mul(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_subJint

```
expand_subJint {
\find(subJint(i,i1))
\replacewith(moduloInt(sub(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_subJlong

```
expand_subJlong {
\find(subJlong(i,i1))
\replacewith(moduloLong(sub(i,i1))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_unaryMinusJint

```
expand_unaryMinusJint {
\find(unaryMinusJint(i))
\replacewith(moduloInt(neg(i))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## expand_unaryMinusJlong

```
expand_unaryMinusJlong {
\find(unaryMinusJlong(i))
\replacewith(moduloLong(neg(i))) 
\heuristics(defOps_expandJNumericOp)
Choices: {}}
```

## false_right

```
false_right {
\find(==>false)
\replacewith([]==>[]) 
\heuristics(concrete)
Choices: {}}
```

## false_to_not_true

```
false_to_not_true {
\find(equals(bo,FALSE))
\replacewith(not(equals(bo,TRUE))) 
\heuristics(concrete, simplify_boolean)
Choices: {}}
```

## finishJavaCardTransaction

```
finishJavaCardTransactionDiamond {
\find(==>diamond_transaction\[{ .. #finishJavaCardTransaction; ... }\] (post))
\replacewith([]==>[diamond(post)]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## finishJavaCardTransaction

```
finishJavaCardTransactionBox {
\find(==>box_transaction\[{ .. #finishJavaCardTransaction; ... }\] (post))
\replacewith([]==>[box(post)]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## firstOfPair

```
firstOfPair {
\find(first(pair(t,t1)))
\replacewith(t) 
\heuristics(concrete)
Choices: {}}
```

## for_to_while

```
for_to_while {
\find(#allmodal ( (modal operator))\[{ .. #forloop ... }\] (post))
\varcond(\newLabel (#innerLabel (program Label)), \newLabel (#outerLabel (program Label)), )
\replacewith(#allmodal ( (modal operator))\[{ .. #for-to-while(#forloop) ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## geq_add

```
geq_add {
\find(==>geq(i0,i1))
\varcond(\notFreeIn(j2 (variable), i1 (int term)), \notFreeIn(j2 (variable), i0 (int term)))
\replacewith([]==>[exists{j2 (variable)}(geq(add(i0,j2),add(i1,j2)))]) 

Choices: {}}
```

## geq_add_one

```
geq_add_one {
\find(geq(i0,i1))
\replacewith(geq(add(i0,Z(1(#))),add(i1,Z(1(#))))) 

Choices: {}}
```

## geq_diff_1

```
geq_diff_1 {
\find(geq(add(i0,Z(1(#))),i0))
\replacewith(true) 
\heuristics(int_arithmetic)
Choices: {}}
```

## geq_to_leq

```
geq_to_leq {
\find(geq(i,i0))
\replacewith(leq(i0,i)) 

Choices: {}}
```

## geq_to_lt

```
geq_to_lt {
\find(geq(i,j))
\replacewith(not(lt(i,j))) 

Choices: {}}
```

## geq_to_lt_alt

```
geq_to_lt_alt {
\find(geq(i,j))
\replacewith(or(gt(i,j),equals(i,j))) 

Choices: {}}
```

## getAnyOfArray2seq

```
getAnyOfArray2seq {
\find(any::seqGet(array2seq(h,a),idx))
\add []==>[and(leq(Z(0(#)),idx),lt(idx,length(a)))] ;
\replacewith(any::select(h,a,arr(idx))) 

Choices: {sequences:on}}
```

## getAnyOfNPermInv

```
getAnyOfNPermInv {
\find(any::seqGet(seqNPermInv(s1),i3))
\add []==>[and(leq(Z(0(#)),i3),lt(i3,seqLen(s1)))] ;
\replacewith(int::seqGet(seqNPermInv(s1),i3)) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## getJavaCardTransient

```
getJavaCardTransient {
\find(==>#allmodal ( (modal operator))\[{ .. 
  #lhs=#jcsystemType.#getTransient(#se)@#jcsystemType; ... }\] (post))
\replacewith([]==>[not(equals(#se,null))]) ;
\replacewith([]==>[update-application(elem-update(#lhs (program LeftHandSide))(int::select(heap,#se,java.lang.Object::<transient>)),#allmodal(post))]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## getOfArray2seq

```
getOfArray2seq {
\find(alpha::seqGet(array2seq(h,a),idx))
\add []==>[and(leq(Z(0(#)),idx),lt(idx,length(a)))] ;
\replacewith(alpha::select(h,a,arr(idx))) 

Choices: {sequences:on}}
```

## getOfMapEmpty

```
getOfMapEmpty {
\find(mapGet(mapEmpty,x))
\sameUpdateLevel\replacewith(mapUndef) 
\heuristics(simplify)
Choices: {}}
```

## getOfMapForeach

```
getOfMapForeach {
\find(mapGet(mapForeach{v (variable)}(b,y),x))
\sameUpdateLevel\replacewith(if-then-else(inDomain(mapForeach{v (variable)}(b,y),x),subst{v (variable)}(alpha::cast(x),y),mapUndef)) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## getOfMapOverride

```
getOfMapOverride {
\find(mapGet(mapOverride(m0,m1),x))
\sameUpdateLevel\replacewith(if-then-else(inDomain(m1,x),mapGet(m1,x),mapGet(m0,x))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## getOfMapRemove

```
getOfMapRemove {
\find(mapGet(mapRemove(m,key),x))
\sameUpdateLevel\replacewith(if-then-else(equals(x,key),mapUndef,mapGet(m,x))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## getOfMapSingleton

```
getOfMapSingleton {
\find(mapGet(mapSingleton(x,y),z))
\sameUpdateLevel\replacewith(if-then-else(equals(x,z),y,mapUndef)) 
\heuristics(simplify)
Choices: {}}
```

## getOfMapUpdate

```
getOfMapUpdate {
\find(mapGet(mapUpdate(m,key,value),x))
\sameUpdateLevel\replacewith(if-then-else(equals(x,key),value,mapGet(m,x))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## getOfNPermInv

```
getOfNPermInv {
\find(int::seqGet(seqNPermInv(s1),i3))
\add []==>[and(and(leq(Z(0(#)),i3),lt(i3,seqLen(s1))),seqNPerm(s1))] ;
\add [and(and(equals(int::seqGet(s1,jsk),i3),leq(Z(0(#)),jsk)),lt(jsk,seqLen(s1)))]==>[] \replacewith(jsk) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## getOfRemoveAny

```
getOfRemoveAny {
\find(alpha::seqGet(seqRemove(s1,i2),i3))
\replacewith(if-then-else(or(lt(i2,Z(0(#))),leq(seqLen(s1),i2)),alpha::seqGet(s1,i3),if-then-else(lt(i3,i2),alpha::seqGet(s1,i3),if-then-else(and(leq(i2,i3),lt(i3,sub(seqLen(s1),Z(1(#))))),alpha::seqGet(s1,add(i3,Z(1(#)))),alpha::cast(seqGetOutside))))) 
\heuristics(simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## getOfRemoveAnyConcrete1

```
getOfRemoveAnyConcrete1 {
\assumes ([geq(seqLen(s1),Z(1(#)))]==>[]) 
\find(alpha::seqGet(seqRemove(s1,sub(seqLen(s1),Z(1(#)))),i3))
\replacewith(if-then-else(lt(i3,sub(seqLen(s1),Z(1(#)))),alpha::seqGet(s1,i3),alpha::cast(seqGetOutside))) 
\heuristics(simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## getOfRemoveAnyConcrete2

```
getOfRemoveAnyConcrete2 {
\assumes ([geq(seqLen(s1),Z(1(#)))]==>[]) 
\find(alpha::seqGet(seqRemove(s1,Z(0(#))),i3))
\replacewith(if-then-else(and(leq(Z(0(#)),i3),lt(i3,sub(seqLen(s1),Z(1(#))))),alpha::seqGet(s1,add(i3,Z(1(#)))),alpha::cast(seqGetOutside))) 
\heuristics(simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## getOfRemoveInt

```
getOfRemoveInt {
\find(int::seqGet(seqRemove(s1,i2),i3))
\replacewith(if-then-else(or(lt(i2,Z(0(#))),leq(seqLen(s1),i2)),int::seqGet(s1,i3),if-then-else(lt(i3,i2),int::seqGet(s1,i3),if-then-else(and(leq(i2,i3),lt(i3,sub(seqLen(s1),Z(1(#))))),int::seqGet(s1,add(i3,Z(1(#)))),int::cast(seqGetOutside))))) 
\heuristics(simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## getOfSeq2Map

```
getOfSeq2Map {
\find(mapGet(seq2map(s),x))
\sameUpdateLevel\replacewith(if-then-else(and(and(equals(int::instance(x),TRUE),leq(Z(0(#)),int::cast(x))),lt(int::cast(x),seqLen(s))),any::seqGet(s,int::cast(x)),mapUndef)) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## getOfSeqConcat

```
getOfSeqConcat {
\find(alpha::seqGet(seqConcat(seq,seq2),idx))
\replacewith(if-then-else(lt(idx,seqLen(seq)),alpha::seqGet(seq,idx),alpha::seqGet(seq2,sub(idx,seqLen(seq))))) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## getOfSeqConcat

```
getOfSeqConcatEQ {
\assumes ([equals(seqConcat(seq,seq2),EQ)]==>[]) 
\find(alpha::seqGet(EQ,idx))
\sameUpdateLevel\replacewith(if-then-else(lt(idx,seqLen(seq)),alpha::seqGet(seq,idx),alpha::seqGet(seq2,sub(idx,seqLen(seq))))) 
\heuristics(simplify_enlarging, no_self_application)
Choices: {sequences:on}}
```

## getOfSeqDef

```
getOfSeqDef {
\find(alpha::seqGet(seqDef{uSub (variable)}(from,to,t),idx))
\varcond(\notFreeIn(uSub (variable), to (int term)), \notFreeIn(uSub (variable), from (int term)))
\replacewith(if-then-else(and(leq(Z(0(#)),idx),lt(idx,sub(to,from))),alpha::cast(subst{uSub (variable)}(add(idx,from),t)),alpha::cast(seqGetOutside))) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## getOfSeqDef

```
getOfSeqDefEQ {
\assumes ([equals(seqDef{uSub (variable)}(from,to,t),EQ)]==>[]) 
\find(alpha::seqGet(EQ,idx))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), to (int term)), \notFreeIn(uSub (variable), from (int term)))
\replacewith(if-then-else(and(leq(Z(0(#)),idx),lt(idx,sub(to,from))),alpha::cast(subst{uSub (variable)}(add(idx,from),t)),alpha::cast(seqGetOutside))) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## getOfSeqReverse

```
getOfSeqReverse {
\find(alpha::seqGet(seqReverse(seq),idx))
\replacewith(alpha::seqGet(seq,sub(sub(seqLen(seq),Z(1(#))),idx))) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## getOfSeqReverse

```
getOfSeqReverseEQ {
\assumes ([equals(seqReverse(seq),EQ)]==>[]) 
\find(alpha::seqGet(EQ,idx))
\sameUpdateLevel\replacewith(alpha::seqGet(seq,sub(sub(seqLen(seq),Z(1(#))),idx))) 
\heuristics(simplify_enlarging, no_self_application)
Choices: {sequences:on}}
```

## getOfSeqSingleton

```
getOfSeqSingleton {
\find(alpha::seqGet(seqSingleton(x),idx))
\replacewith(if-then-else(equals(idx,Z(0(#))),alpha::cast(x),alpha::cast(seqGetOutside))) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## getOfSeqSingleton

```
getOfSeqSingletonEQ {
\assumes ([equals(seqSingleton(x),EQ)]==>[]) 
\find(alpha::seqGet(EQ,idx))
\sameUpdateLevel\replacewith(if-then-else(equals(idx,Z(0(#))),alpha::cast(x),alpha::cast(seqGetOutside))) 
\heuristics(simplify_enlarging, no_self_application)
Choices: {sequences:on}}
```

## getOfSeqSingletonConcrete

```
getOfSeqSingletonConcrete {
\find(alpha::seqGet(seqSingleton(x),Z(0(#))))
\replacewith(alpha::cast(x)) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## getOfSeqSub

```
getOfSeqSub {
\find(alpha::seqGet(seqSub(seq,from,to),idx))
\replacewith(if-then-else(and(leq(Z(0(#)),idx),lt(idx,sub(to,from))),alpha::seqGet(seq,add(idx,from)),alpha::cast(seqGetOutside))) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## getOfSeqSub

```
getOfSeqSubEQ {
\assumes ([equals(seqSub(seq,from,to),EQ)]==>[]) 
\find(alpha::seqGet(EQ,idx))
\sameUpdateLevel\replacewith(if-then-else(and(leq(Z(0(#)),idx),lt(idx,sub(to,from))),alpha::seqGet(seq,add(idx,from)),alpha::cast(seqGetOutside))) 
\heuristics(simplify_enlarging, no_self_application)
Choices: {sequences:on}}
```

## getOfSwap

```
getOfSwap {
\find(alpha::seqGet(seqSwap(s1,iv,jv),idx))
\replacewith(if-then-else(not(and(and(and(leq(Z(0(#)),iv),leq(Z(0(#)),jv)),lt(iv,seqLen(s1))),lt(jv,seqLen(s1)))),alpha::seqGet(s1,idx),if-then-else(equals(idx,iv),alpha::seqGet(s1,jv),if-then-else(equals(idx,jv),alpha::seqGet(s1,iv),alpha::seqGet(s1,idx))))) 
\heuristics(simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## ghostDeclaration

```
variableDeclarationGhost {
\find(#allmodal ( (modal operator))\[{ .. ghost#t #v0; ... }\] (post))
\addprogvars {#v0 (program Variable)} \replacewith(#allmodal(post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## ghostDeclaration

```
variableDeclarationGhostAssign {
\find(#allmodal ( (modal operator))\[{ .. ghost#t #v0 = #vi; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. ghost#t #v0;#v0=#vi; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## greater

```
greater {
\find(gt(i,i0))
\replacewith(lt(i0,i)) 

Choices: {}}
```

## greater than distinction

```
greater_than_comparison_new {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0>#se1; ... }\] (post))
\replacewith(if-then-else(gt(#se0,#se1),#allmodal ( (modal operator))\[{ .. #lhs=true; ... }\] (post),#allmodal ( (modal operator))\[{ .. #lhs=false; ... }\] (post))) 
\heuristics(split_if, simplify_prog, obsolete)
Choices: {programRules:Java}}
```

## greater than distinction

```
greater_than_comparison_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0>#se1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(gt(#se0,#se1),TRUE,FALSE)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## greater-or-equal than distinction

```
greater_equal_than_comparison_new {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0>=#se1; ... }\] (post))
\replacewith(if-then-else(geq(#se0,#se1),#allmodal ( (modal operator))\[{ .. #lhs=true; ... }\] (post),#allmodal ( (modal operator))\[{ .. #lhs=false; ... }\] (post))) 
\heuristics(split_if, simplify_prog, obsolete)
Choices: {programRules:Java}}
```

## greater-or-equal than distinction

```
greater_equal_than_comparison_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0>=#se1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(geq(#se0,#se1),TRUE,FALSE)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## greater_add

```
greater_add {
\find(==>gt(i0,i1))
\varcond(\notFreeIn(j2 (variable), i1 (int term)), \notFreeIn(j2 (variable), i0 (int term)))
\replacewith([]==>[exists{j2 (variable)}(gt(add(i0,j2),add(i1,j2)))]) 

Choices: {}}
```

## greater_add_one

```
greater_add_one {
\find(gt(i0,i1))
\replacewith(gt(add(i0,Z(1(#))),add(i1,Z(1(#))))) 

Choices: {}}
```

## greater_literals

```
greater_literals {
\find(gt(Z(iz),Z(jz)))
\replacewith(#greater(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## greater_or_equal

```
compound_greater_equal_than_comparison_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse0>=#se; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse0 (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse0) #v0 = #nse0;#lhs=#v0>=#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## greater_or_equal

```
compound_greater_equal_than_comparison_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e>=#nse0; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse0 (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse0) #v1 = #nse0;#lhs=#v0>=#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## greater_than

```
compound_greater_than_comparison_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse0>#se; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse0 (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse0) #v0 = #nse0;#lhs=#v0>#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## greater_than

```
compound_greater_than_comparison_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e>#nse0; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse0 (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse0) #v1 = #nse0;#lhs=#v0>#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## gt_diff_1

```
gt_diff_1 {
\find(gt(add(i0,Z(1(#))),i0))
\replacewith(true) 
\heuristics(int_arithmetic)
Choices: {}}
```

## gt_to_lt

```
gt_to_lt {
\find(gt(i,i0))
\replacewith(lt(i0,i)) 

Choices: {}}
```

## hashCodeBase

```
hashCodeBase {
\find(clHashCode(seqEmpty))
\replacewith(Z(0(#))) 
\heuristics(simplify_literals)
Choices: {Strings:on}}
```

## hideAuxiliaryEq

```
hideAuxiliaryEq {
\find(equals(result,auxiliarySK)==>)
\addrules [replaceKnownAuxiliaryConstant {
\find(auxiliarySK)
\inSequentState\replacewith(result) 
\heuristics(concrete)
Choices: {}}] \replacewith([]==>[]) 
\heuristics(hide_auxiliary_eq)
Choices: {programRules:Java}}
```

## hideAuxiliaryEqConcrete

```
hideAuxiliaryEqConcrete {
\find(equals(auxiliarySK,TRUE)==>)
\addrules [replaceKnownAuxiliaryConstant {
\find(auxiliarySK)
\inSequentState\replacewith(TRUE) 
\heuristics(concrete)
Choices: {}}] \replacewith([]==>[]) 
\heuristics(hide_auxiliary_eq_const)
Choices: {programRules:Java}}
```

## hideAuxiliaryEqConcrete2

```
hideAuxiliaryEqConcrete2 {
\find(==>equals(auxiliarySK,TRUE))
\addrules [replaceKnownAuxiliaryConstant {
\find(auxiliarySK)
\inSequentState\replacewith(FALSE) 
\heuristics(concrete)
Choices: {}}] \replacewith([]==>[]) 
\heuristics(hide_auxiliary_eq_const)
Choices: {programRules:Java}}
```

## hide_left

```
hide_left {
\find(b==>)
\addrules [insert_hidden {
\add [b]==>[] 

Choices: {}}] \replacewith([]==>[]) 

Choices: {}}
```

## hide_right

```
hide_right {
\find(==>b)
\addrules [insert_hidden {
\add []==>[b] 

Choices: {}}] \replacewith([]==>[]) 

Choices: {}}
```

## i_minus_i_is_zero

```
i_minus_i_is_zero {
\find(sub(i,i))
\replacewith(Z(0(#))) 

Choices: {}}
```

## ifElse

```
if {
\find(#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
 ... }\] (post))
\replacewith(if-then-else(equals(#se,TRUE),#allmodal ( (modal operator))\[{ .. #s0 ... }\] (post),#allmodal(post))) 

Choices: {programRules:Java}}
```

## ifElse

```
ifElse {
\find(#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
  else 
    #s1
 ... }\] (post))
\replacewith(if-then-else(equals(#se,TRUE),#allmodal ( (modal operator))\[{ .. #s0 ... }\] (post),#allmodal ( (modal operator))\[{ .. #s1 ... }\] (post))) 

Choices: {programRules:Java}}
```

## ifElseFalse

```
ifElseFalse {
\assumes ([equals(#se,FALSE)]==>[]) 
\find(==>#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
  else 
    #s1
 ... }\] (post))
\replacewith([]==>[#allmodal ( (modal operator))\[{ .. #s1 ... }\] (post)]) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## ifElseSkipElse

```
ifElseSkipElse {
\find(#allmodal ( (modal operator))\[{ .. #loc=true;if (#loc)
    #s0
  else 
    #s1
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #loc=true;#s0 ... }\] (post)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## ifElseSkipElse

```
ifElseSkipElseConditionInBlock {
\find(#allmodal ( (modal operator))\[{ ..  {#loc=true;}if (#loc)
    #s0
  else 
    #s1
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {#loc=true;}#s0 ... }\] (post)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## ifElseSkipThen

```
ifElseSkipThen {
\find(#allmodal ( (modal operator))\[{ .. #loc=false;if (#loc)
    #s0
  else 
    #s1
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #loc=false;#s1 ... }\] (post)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## ifElseSkipThen

```
ifElseSkipThenConditionInBlock {
\find(#allmodal ( (modal operator))\[{ ..  {#loc=false;}if (#loc)
    #s0
  else 
    #s1
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {#loc=false;}#s1 ... }\] (post)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## ifElseSplit

```
ifElseSplit {
\find(==>#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
  else 
    #s1
 ... }\] (post))
\add [equals(#se,FALSE)]==>[] \replacewith([]==>[#allmodal ( (modal operator))\[{ .. #s1 ... }\] (post)]) ;
\add [equals(#se,TRUE)]==>[] \replacewith([]==>[#allmodal ( (modal operator))\[{ .. #s0 ... }\] (post)]) 
\heuristics(split_if)
Choices: {programRules:Java}}
```

## ifElseSplit

```
ifSplit {
\find(==>#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
 ... }\] (post))
\add [equals(#se,FALSE)]==>[] \replacewith([]==>[#allmodal(post)]) ;
\add [equals(#se,TRUE)]==>[] \replacewith([]==>[#allmodal ( (modal operator))\[{ .. #s0 ... }\] (post)]) 
\heuristics(split_if)
Choices: {programRules:Java}}
```

## ifElseSplitLeft

```
ifElseSplitLeft {
\find(#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
  else 
    #s1
 ... }\] (post)==>)
\add [equals(#se,FALSE)]==>[] \replacewith([#allmodal ( (modal operator))\[{ .. #s1 ... }\] (post)]==>[]) ;
\add [equals(#se,TRUE)]==>[] \replacewith([#allmodal ( (modal operator))\[{ .. #s0 ... }\] (post)]==>[]) 
\heuristics(split_if)
Choices: {programRules:Java}}
```

## ifElseTrue

```
ifElseTrue {
\assumes ([equals(#se,TRUE)]==>[]) 
\find(==>#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
  else 
    #s1
 ... }\] (post))
\replacewith([]==>[#allmodal ( (modal operator))\[{ .. #s0 ... }\] (post)]) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## ifElseUnfold

```
ifUnfold {
\find(#allmodal ( (modal operator))\[{ .. if (#nse)
    #s0
 ... }\] (post))
\varcond(\new(#boolv (program Variable), boolean))
\replacewith(#allmodal ( (modal operator))\[{ .. boolean #boolv;#boolv=#nse;if (#boolv)
    #s0
 ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## ifElseUnfold

```
ifElseUnfold {
\find(#allmodal ( (modal operator))\[{ .. if (#nse)
    #s0
  else 
    #s1
 ... }\] (post))
\varcond(\new(#boolv (program Variable), boolean))
\replacewith(#allmodal ( (modal operator))\[{ .. boolean #boolv;#boolv=#nse;if (#boolv)
    #s0
  else  {
    #s1
  }
 ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## ifEnterThen

```
ifEnterThen {
\find(#allmodal ( (modal operator))\[{ .. #loc=true;if (#loc)
    #s0
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #loc=true;#s0 ... }\] (post)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## ifEnterThen

```
ifEnterThenConditionInBlock {
\find(#allmodal ( (modal operator))\[{ ..  {#loc=true;}if (#loc)
    #s0
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {#loc=true;}#s0 ... }\] (post)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## ifEqualsInteger

```
ifEqualsInteger {
\find(equals(if-then-else(phi,x,y),Z(iz)))
\replacewith(or(and(phi,equals(x,Z(iz))),and(not(phi),equals(y,Z(iz))))) 
\heuristics(simplify)
Choices: {}}
```

## ifEqualsNull

```
ifEqualsNull {
\find(equals(if-then-else(phi,x,y),null))
\replacewith(or(and(phi,equals(x,null)),and(not(phi),equals(y,null)))) 
\heuristics(simplify)
Choices: {}}
```

## ifEqualsTRUE

```
ifEqualsTRUE {
\find(equals(if-then-else(phi,x,y),TRUE))
\replacewith(or(and(phi,equals(x,TRUE)),and(not(phi),equals(y,TRUE)))) 
\heuristics(simplify)
Choices: {}}
```

## ifExthenelse1_eq

```
ifExthenelse1_eq {
\find(ifExThenElse{intVar (variable)}(equals(intVar,t),then,else))
\varcond(\notFreeIn(intVar (variable), t (int term)))
\replacewith(subst{intVar (variable)}(t,then)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_eq

```
ifExthenelse1_eq_for {
\find(ifExThenElse{intVar (variable)}(equals(intVar,t),b,c))
\varcond(\notFreeIn(intVar (variable), t (int term)))
\replacewith(subst{intVar (variable)}(t,b)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_eq

```
ifExthenelse1_eq2 {
\find(ifExThenElse{intVar (variable)}(equals(t,intVar),then,else))
\varcond(\notFreeIn(intVar (variable), t (int term)))
\replacewith(subst{intVar (variable)}(t,then)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_eq

```
ifExthenelse1_eq2_for {
\find(ifExThenElse{intVar (variable)}(equals(t,intVar),b,c))
\varcond(\notFreeIn(intVar (variable), t (int term)))
\replacewith(subst{intVar (variable)}(t,b)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_eq

```
ifExthenelse1_eq_for_phi {
\find(ifExThenElse{intVar (variable)}(and(phi,equals(intVar,t)),b,c))
\varcond(\notFreeIn(intVar (variable), t (int term)))
\replacewith(if-then-else(subst{intVar (variable)}(t,phi),subst{intVar (variable)}(t,b),c)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_eq

```
ifExthenelse1_eq2_phi {
\find(ifExThenElse{intVar (variable)}(and(phi,equals(t,intVar)),then,else))
\varcond(\notFreeIn(intVar (variable), t (int term)))
\replacewith(if-then-else(subst{intVar (variable)}(t,phi),subst{intVar (variable)}(t,then),else)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_eq

```
ifExthenelse1_eq2_for_phi {
\find(ifExThenElse{intVar (variable)}(and(phi,equals(t,intVar)),b,c))
\varcond(\notFreeIn(intVar (variable), t (int term)))
\replacewith(if-then-else(subst{intVar (variable)}(t,phi),subst{intVar (variable)}(t,b),c)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_eq_phi

```
ifExthenelse1_eq_phi {
\find(ifExThenElse{intVar (variable)}(and(phi,equals(intVar,t)),then,else))
\varcond(\notFreeIn(intVar (variable), t (int term)))
\replacewith(if-then-else(subst{intVar (variable)}(t,phi),subst{intVar (variable)}(t,then),else)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_false

```
ifExthenelse1_false {
\find(ifExThenElse{intVar (variable)}(false,then,else))
\replacewith(else) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_false

```
ifExthenelse1_false_for {
\find(ifExThenElse{intVar (variable)}(false,b,c))
\replacewith(c) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_min

```
ifExthenelse1_min {
\find(ifExThenElse{intVar (variable)}(phi,then,else))
\sameUpdateLevel\varcond(\notFreeIn(intVar (variable), phi (formula)))
\replacewith(if-then-else(phi,subst{intVar (variable)}(Z(0(#)),then),else)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_min

```
ifExthenelse1_min_for {
\find(ifExThenElse{intVar (variable)}(phi,b,c))
\sameUpdateLevel\varcond(\notFreeIn(intVar (variable), phi (formula)))
\replacewith(if-then-else(phi,subst{intVar (variable)}(Z(0(#)),b),c)) 
\heuristics(concrete)
Choices: {}}
```

## ifExthenelse1_solve

```
ifExthenelse1_solve {
\find(ifExThenElse{intVar (variable)}(phi,then,else))
\sameUpdateLevel\varcond(\notFreeIn(intVar (variable), intValue (int term)))
\add []==>[and(subst{intVar (variable)}(intValue,phi),all{intVar (variable)}(imp(phi,wellOrderLeqInt(intValue,intVar))))] \replacewith(ifExThenElse{intVar (variable)}(phi,then,else)) ;
\replacewith(subst{intVar (variable)}(intValue,then)) 

Choices: {}}
```

## ifExthenelse1_solve

```
ifExthenelse1_solve_for {
\find(ifExThenElse{intVar (variable)}(phi,b,c))
\sameUpdateLevel\varcond(\notFreeIn(intVar (variable), intValue (int term)))
\add []==>[and(subst{intVar (variable)}(intValue,phi),all{intVar (variable)}(imp(phi,wellOrderLeqInt(intValue,intVar))))] \replacewith(ifExThenElse{intVar (variable)}(phi,b,c)) ;
\replacewith(subst{intVar (variable)}(intValue,b)) 

Choices: {}}
```

## ifExthenelse1_split

```
ifExthenelse1_split {
\find(ifExThenElse{intVar (variable)}(phi,then,else))
\sameUpdateLevel\varcond(\notFreeIn(intVar (variable), intSk (int skolem term)))
\add []==>[exists{intVar (variable)}(phi)] \replacewith(else) ;
\add [subst{intVar (variable)}(intSk,phi),all{intVar (variable)}(imp(phi,wellOrderLeqInt(intSk,intVar)))]==>[] \replacewith(subst{intVar (variable)}(intSk,then)) 
\heuristics(split_cond)
Choices: {}}
```

## ifExthenelse1_split

```
ifExthenelse1_split_for {
\find(ifExThenElse{intVar (variable)}(phi,b,c))
\sameUpdateLevel\varcond(\notFreeIn(intVar (variable), intSk (int skolem term)))
\add []==>[exists{intVar (variable)}(phi)] \replacewith(c) ;
\add [subst{intVar (variable)}(intSk,phi),all{intVar (variable)}(imp(phi,wellOrderLeqInt(intSk,intVar)))]==>[] \replacewith(subst{intVar (variable)}(intSk,b)) 
\heuristics(split_cond)
Choices: {}}
```

## ifExthenelse1_unused_var

```
ifExthenelse1_unused_var {
\find(ifExThenElse{intVar (variable)}(phi,then,else))
\varcond(\notFreeIn(intVar (variable), then (G2 term)))
\replacewith(if-then-else(exists{intVar (variable)}(phi),then,else)) 
\heuristics(simplify)
Choices: {}}
```

## ifExthenelse1_unused_var

```
ifExthenelse1_unused_var_for {
\find(ifExThenElse{intVar (variable)}(phi,b,c))
\varcond(\notFreeIn(intVar (variable), b (formula)))
\replacewith(if-then-else(exists{intVar (variable)}(phi),b,c)) 
\heuristics(simplify)
Choices: {}}
```

## ifFalse

```
ifFalse {
\assumes ([equals(#se,FALSE)]==>[]) 
\find(==>#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
 ... }\] (post))
\replacewith([]==>[#allmodal(post)]) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## ifSkipThen

```
ifSkipThen {
\find(#allmodal ( (modal operator))\[{ .. #loc=false;if (#loc)
    #s0
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #loc=false; ... }\] (post)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## ifSkipThen

```
ifSkipThenConditionInBlock {
\find(#allmodal ( (modal operator))\[{ ..  {#loc=false;}if (#loc)
    #s0
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {#loc=false;} ... }\] (post)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## ifSplitLeft

```
ifSplitLeft {
\find(#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
 ... }\] (post)==>)
\add [equals(#se,FALSE)]==>[] \replacewith([#allmodal(post)]==>[]) ;
\add [equals(#se,TRUE)]==>[] \replacewith([#allmodal ( (modal operator))\[{ .. #s0 ... }\] (post)]==>[]) 
\heuristics(split_if)
Choices: {programRules:Java}}
```

## ifTrue

```
ifTrue {
\assumes ([equals(#se,TRUE)]==>[]) 
\find(==>#allmodal ( (modal operator))\[{ .. if (#se)
    #s0
 ... }\] (post))
\replacewith([]==>[#allmodal ( (modal operator))\[{ .. #s0 ... }\] (post)]) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## ifthenelse_concrete

```
ifthenelse_concrete {
\find(if-then-else(phi,true,false))
\replacewith(phi) 
\heuristics(concrete)
Choices: {}}
```

## ifthenelse_concrete

```
ifthenelse_concrete2 {
\find(if-then-else(phi,false,true))
\replacewith(not(phi)) 
\heuristics(concrete)
Choices: {}}
```

## ifthenelse_concrete

```
ifthenelse_concrete3 {
\find(equals(if-then-else(phi,then,else),then))
\replacewith(or(phi,equals(else,then))) 
\heuristics(concrete)
Choices: {}}
```

## ifthenelse_concrete

```
ifthenelse_concrete4 {
\find(equals(if-then-else(phi,then,else),else))
\replacewith(or(not(phi),equals(then,else))) 
\heuristics(concrete)
Choices: {}}
```

## ifthenelse_false

```
ifthenelse_false {
\find(if-then-else(false,then,else))
\replacewith(else) 
\heuristics(concrete)
Choices: {}}
```

## ifthenelse_false

```
ifthenelse_false_for {
\find(if-then-else(false,b,c))
\replacewith(c) 
\heuristics(concrete)
Choices: {}}
```

## ifthenelse_negated

```
ifthenelse_negated {
\find(if-then-else(not(phi),then,else))
\replacewith(if-then-else(phi,else,then)) 
\heuristics(simplify)
Choices: {}}
```

## ifthenelse_negated

```
ifthenelse_negated_for {
\find(if-then-else(not(phi),b,c))
\replacewith(if-then-else(phi,c,b)) 
\heuristics(simplify)
Choices: {}}
```

## ifthenelse_same_branches

```
ifthenelse_same_branches {
\find(if-then-else(phi,then,then))
\replacewith(then) 
\heuristics(concrete)
Choices: {}}
```

## ifthenelse_same_branches

```
ifthenelse_same_branches_for {
\find(if-then-else(phi,b,b))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## ifthenelse_split

```
ifthenelse_split {
\find(if-then-else(phi,then,else))
\sameUpdateLevel\add []==>[phi] \replacewith(else) ;
\add [phi]==>[] \replacewith(then) 
\heuristics(split_cond)
Choices: {}}
```

## ifthenelse_split

```
ifthenelse_split_for {
\find(if-then-else(phi,b,c))
\sameUpdateLevel\add []==>[phi] \replacewith(c) ;
\add [phi]==>[] \replacewith(b) 
\heuristics(split_cond)
Choices: {}}
```

## ifthenelse_to_or_for

```
ifthenelse_to_or_for {
\find(if-then-else(phi,b,c))
\replacewith(and(or(not(phi),b),or(phi,c))) 
\heuristics(notHumanReadable, cnf_expandIfThenElse, conjNormalForm)
Choices: {}}
```

## ifthenelse_to_or_for2

```
ifthenelse_to_or_for2 {
\find(not(if-then-else(phi,b,c)))
\replacewith(and(or(not(phi),not(b)),or(phi,not(c)))) 
\heuristics(notHumanReadable, cnf_expandIfThenElse, conjNormalForm)
Choices: {}}
```

## ifthenelse_to_or_left

```
ifthenelse_to_or_left {
\find(equals(if-then-else(phi,then,else),t))
\replacewith(and(or(not(phi),equals(then,t)),or(phi,equals(else,t)))) 
\heuristics(notHumanReadable, cnf_expandIfThenElse, conjNormalForm)
Choices: {}}
```

## ifthenelse_to_or_left2

```
ifthenelse_to_or_left2 {
\find(not(equals(if-then-else(phi,then,else),t)))
\replacewith(and(or(not(phi),not(equals(then,t))),or(phi,not(equals(else,t))))) 
\heuristics(notHumanReadable, cnf_expandIfThenElse, conjNormalForm)
Choices: {}}
```

## ifthenelse_to_or_right

```
ifthenelse_to_or_right {
\find(equals(t,if-then-else(phi,then,else)))
\replacewith(and(or(not(phi),equals(t,then)),or(phi,equals(t,else)))) 
\heuristics(notHumanReadable, cnf_expandIfThenElse, conjNormalForm)
Choices: {}}
```

## ifthenelse_to_or_right2

```
ifthenelse_to_or_right2 {
\find(not(equals(t,if-then-else(phi,then,else))))
\replacewith(and(or(not(phi),not(equals(t,then))),or(phi,not(equals(t,else))))) 
\heuristics(notHumanReadable, cnf_expandIfThenElse, conjNormalForm)
Choices: {}}
```

## ifthenelse_true

```
ifthenelse_true {
\find(if-then-else(true,then,else))
\replacewith(then) 
\heuristics(concrete)
Choices: {}}
```

## ifthenelse_true

```
ifthenelse_true_for {
\find(if-then-else(true,b,c))
\replacewith(b) 
\heuristics(concrete)
Choices: {}}
```

## impLeft

```
impLeft {
\find(imp(b,c)==>)
\replacewith([c]==>[]) ;
\replacewith([]==>[b]) 
\heuristics(beta)
Choices: {}}
```

## impRight

```
impRight {
\find(==>imp(b,c))
\replacewith([b]==>[c]) 
\heuristics(alpha)
Choices: {}}
```

## inByte

```
inByte {
\find(inByte(i))
\replacewith(true) 
\heuristics(concrete)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## inChar

```
inChar {
\find(inChar(i))
\replacewith(true) 
\heuristics(concrete)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## inDomainConcrete

```
inDomainConcrete {
\assumes ([]==>[equals(mapUndef,y)]) 
\find(equals(mapGet(m,x),y)==>)
\add [inDomain(m,x)]==>[] 
\heuristics(inReachableStateImplication)
Choices: {}}
```

## inDomainOfMapEmpty

```
inDomainOfMapEmpty {
\find(inDomain(mapEmpty,x))
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## inDomainOfMapForeach

```
inDomainOfMapForeach {
\find(inDomain(mapForeach{v (variable)}(b,y),x))
\replacewith(and(equals(subst{v (variable)}(alpha::cast(x),b),TRUE),equals(alpha::instance(x),TRUE))) 
\heuristics(simplify)
Choices: {}}
```

## inDomainOfMapOverride

```
inDomainOfMapOverride {
\find(inDomain(mapOverride(m0,m1),x))
\replacewith(or(inDomain(m0,x),inDomain(m1,x))) 
\heuristics(simplify)
Choices: {}}
```

## inDomainOfMapRemove

```
inDomainOfMapRemove {
\find(inDomain(mapRemove(m,key),x))
\replacewith(and(inDomain(m,x),not(equals(x,key)))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## inDomainOfMapSingleton

```
inDomainOfMapSingleton {
\find(inDomain(mapSingleton(x,y),z))
\replacewith(equals(x,z)) 
\heuristics(simplify)
Choices: {}}
```

## inDomainOfMapUpdate

```
inDomainOfMapUpdate {
\find(inDomain(mapUpdate(m,key,value),x))
\replacewith(or(inDomain(m,x),equals(x,key))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## inDomainOfSeq2Map

```
inDomainOfSeq2Map {
\find(inDomain(seq2map(s),x))
\replacewith(and(and(equals(int::instance(x),TRUE),leq(Z(0(#)),int::cast(x))),lt(int::cast(x),seqLen(s)))) 
\heuristics(simplify)
Choices: {}}
```

## inEqSimp_and_antiSymm0

```
inEqSimp_and_antiSymm0 {
\find(and(leq(i,i0),geq(i,i0)))
\replacewith(equals(i,i0)) 
\heuristics(inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_antiSymm1

```
inEqSimp_and_antiSymm1 {
\find(and(and(b,leq(i,i0)),geq(i,i0)))
\replacewith(and(b,equals(i,i0))) 
\heuristics(inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_contradInEq0

```
inEqSimp_and_contradInEq0 {
\find(and(leq(contradLeft,contradRightSmaller),geq(contradLeft,contradRightBigger)))
\replacewith(and(and(leq(contradLeft,contradRightSmaller),geq(contradLeft,contradRightBigger)),geq(contradRightSmaller,contradRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_and_contradInEqs, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_contradInEq1

```
inEqSimp_and_contradInEq1 {
\find(and(and(b,leq(contradLeft,contradRightSmaller)),geq(contradLeft,contradRightBigger)))
\replacewith(and(and(and(b,leq(contradLeft,contradRightSmaller)),geq(contradLeft,contradRightBigger)),geq(contradRightSmaller,contradRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_and_contradInEqs, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_strengthen0

```
inEqSimp_and_strengthen0 {
\find(and(leq(strengthenLeft,strengthenRight),not(equals(strengthenLeft,strengthenRight))))
\replacewith(leq(strengthenLeft,add(Z(neglit(1(#))),strengthenRight))) 
\heuristics(notHumanReadable, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_strengthen1

```
inEqSimp_and_strengthen1 {
\find(and(geq(strengthenLeft,strengthenRight),not(equals(strengthenLeft,strengthenRight))))
\replacewith(geq(strengthenLeft,add(Z(1(#)),strengthenRight))) 
\heuristics(notHumanReadable, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_strengthen2

```
inEqSimp_and_strengthen2 {
\find(and(and(b,leq(strengthenLeft,strengthenRight)),not(equals(strengthenLeft,strengthenRight))))
\replacewith(and(b,leq(strengthenLeft,add(Z(neglit(1(#))),strengthenRight)))) 
\heuristics(notHumanReadable, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_strengthen3

```
inEqSimp_and_strengthen3 {
\find(and(and(b,geq(strengthenLeft,strengthenRight)),not(equals(strengthenLeft,strengthenRight))))
\replacewith(and(b,geq(strengthenLeft,add(Z(1(#)),strengthenRight)))) 
\heuristics(notHumanReadable, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_subsumption0

```
inEqSimp_and_subsumption0 {
\find(and(leq(subsumLeft,subsumRightSmaller),leq(subsumLeft,subsumRightBigger)))
\replacewith(and(leq(subsumLeft,subsumRightSmaller),or(leq(subsumRightSmaller,subsumRightBigger),leq(subsumLeft,subsumRightBigger)))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_subsumption1

```
inEqSimp_and_subsumption1 {
\find(and(and(b,leq(subsumLeft,subsumRightSmaller)),leq(subsumLeft,subsumRightBigger)))
\replacewith(and(and(b,leq(subsumLeft,subsumRightSmaller)),or(leq(subsumRightSmaller,subsumRightBigger),leq(subsumLeft,subsumRightBigger)))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_subsumption2

```
inEqSimp_and_subsumption2 {
\find(and(geq(subsumLeft,subsumRightSmaller),geq(subsumLeft,subsumRightBigger)))
\replacewith(and(or(leq(subsumRightSmaller,subsumRightBigger),geq(subsumLeft,subsumRightSmaller)),geq(subsumLeft,subsumRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_subsumption3

```
inEqSimp_and_subsumption3 {
\find(and(and(b,geq(subsumLeft,subsumRightSmaller)),geq(subsumLeft,subsumRightBigger)))
\replacewith(and(and(b,or(leq(subsumRightSmaller,subsumRightBigger),geq(subsumLeft,subsumRightSmaller))),geq(subsumLeft,subsumRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_subsumption4

```
inEqSimp_and_subsumption4 {
\find(and(leq(subsumLeft,subsumRightSmaller),not(equals(subsumLeft,subsumRightBigger))))
\replacewith(and(leq(subsumLeft,subsumRightSmaller),or(lt(subsumRightSmaller,subsumRightBigger),not(equals(subsumLeft,subsumRightBigger))))) 
\heuristics(notHumanReadable, inEqSimp_and_subsumptionEq, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_subsumption5

```
inEqSimp_and_subsumption5 {
\find(and(and(b,leq(subsumLeft,subsumRightSmaller)),not(equals(subsumLeft,subsumRightBigger))))
\replacewith(and(and(b,leq(subsumLeft,subsumRightSmaller)),or(lt(subsumRightSmaller,subsumRightBigger),not(equals(subsumLeft,subsumRightBigger))))) 
\heuristics(notHumanReadable, inEqSimp_and_subsumptionEq, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_subsumption6

```
inEqSimp_and_subsumption6 {
\find(and(geq(subsumLeft,subsumRightBigger),not(equals(subsumLeft,subsumRightSmaller))))
\replacewith(and(geq(subsumLeft,subsumRightBigger),or(lt(subsumRightSmaller,subsumRightBigger),not(equals(subsumLeft,subsumRightSmaller))))) 
\heuristics(notHumanReadable, inEqSimp_and_subsumptionEq, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_and_subsumption7

```
inEqSimp_and_subsumption7 {
\find(and(and(b,geq(subsumLeft,subsumRightBigger)),not(equals(subsumLeft,subsumRightSmaller))))
\replacewith(and(and(b,geq(subsumLeft,subsumRightBigger)),or(lt(subsumRightSmaller,subsumRightBigger),not(equals(subsumLeft,subsumRightSmaller))))) 
\heuristics(notHumanReadable, inEqSimp_and_subsumptionEq, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_antiSymm

```
inEqSimp_antiSymm {
\assumes ([leq(i,i0)]==>[]) 
\find(geq(i,i0)==>)
\add [equals(i,i0)]==>[] 
\heuristics(inEqSimp_antiSymm, inEqSimp_saturate)
Choices: {}}
```

## inEqSimp_commuteGeq

```
inEqSimp_commuteGeq {
\find(geq(commLeft,commRight))
\replacewith(leq(commRight,commLeft)) 
\heuristics(inEqSimp_commute, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_commuteLeq

```
inEqSimp_commuteLeq {
\find(leq(commLeft,commRight))
\replacewith(geq(commRight,commLeft)) 
\heuristics(inEqSimp_commute, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_contradEq3

```
inEqSimp_contradEq3 {
\assumes ([leq(contradLeft,contradRightSmaller)]==>[]) 
\find(equals(contradLeft,contradRightBigger))
\sameUpdateLevel\replacewith(and(geq(add(contradRightSmaller,mul(Z(neglit(1(#))),contradRightBigger)),Z(0(#))),equals(contradLeft,contradRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_contradEqs, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_contradEq7

```
inEqSimp_contradEq7 {
\assumes ([geq(contradLeft,contradRightBigger)]==>[]) 
\find(equals(contradLeft,contradRightSmaller))
\sameUpdateLevel\replacewith(and(leq(add(contradRightBigger,mul(Z(neglit(1(#))),contradRightSmaller)),Z(0(#))),equals(contradLeft,contradRightSmaller))) 
\heuristics(notHumanReadable, inEqSimp_contradEqs, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_contradInEq0

```
inEqSimp_contradInEq0 {
\assumes ([leq(contradLeft,contradRightSmaller)]==>[]) 
\find(geq(contradLeft,contradRightBigger))
\sameUpdateLevel\replacewith(and(geq(contradRightSmaller,contradRightBigger),geq(contradLeft,contradRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_contradInEqs, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_contradInEq1

```
inEqSimp_contradInEq1 {
\assumes ([geq(contradLeft,contradRightBigger)]==>[]) 
\find(leq(contradLeft,contradRightSmaller))
\sameUpdateLevel\replacewith(and(geq(contradRightSmaller,contradRightBigger),leq(contradLeft,contradRightSmaller))) 
\heuristics(notHumanReadable, inEqSimp_contradInEqs, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_contradInEq2

```
inEqSimp_contradInEq2 {
\assumes ([leq(mul(contradLeft,contradCoeffSmaller),contradRightSmaller)]==>[]) 
\find(geq(mul(contradLeft,contradCoeffBigger),contradRightBigger))
\sameUpdateLevel\replacewith(and(imp(gt(contradCoeffSmaller,Z(0(#))),imp(gt(contradCoeffBigger,Z(0(#))),geq(mul(contradCoeffBigger,contradRightSmaller),mul(contradCoeffSmaller,contradRightBigger)))),geq(mul(contradLeft,contradCoeffBigger),contradRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_contradInEqs, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_contradInEq3

```
inEqSimp_contradInEq3 {
\assumes ([leq(contradLeft,contradRightSmaller)]==>[]) 
\find(geq(mul(contradLeft,contradCoeffBigger),contradRightBigger))
\sameUpdateLevel\replacewith(and(imp(gt(contradCoeffBigger,Z(0(#))),geq(mul(contradCoeffBigger,contradRightSmaller),contradRightBigger)),geq(mul(contradLeft,contradCoeffBigger),contradRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_contradInEqs, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_contradInEq4

```
inEqSimp_contradInEq4 {
\assumes ([geq(mul(contradLeft,contradCoeffBigger),contradRightBigger)]==>[]) 
\find(leq(mul(contradLeft,contradCoeffSmaller),contradRightSmaller))
\sameUpdateLevel\replacewith(and(imp(gt(contradCoeffSmaller,Z(0(#))),imp(gt(contradCoeffBigger,Z(0(#))),geq(mul(contradCoeffBigger,contradRightSmaller),mul(contradCoeffSmaller,contradRightBigger)))),leq(mul(contradLeft,contradCoeffSmaller),contradRightSmaller))) 
\heuristics(notHumanReadable, inEqSimp_contradInEqs, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_contradInEq5

```
inEqSimp_contradInEq5 {
\assumes ([geq(contradLeft,contradRightBigger)]==>[]) 
\find(leq(mul(contradLeft,contradCoeffSmaller),contradRightSmaller))
\sameUpdateLevel\replacewith(and(imp(gt(contradCoeffSmaller,Z(0(#))),geq(contradRightSmaller,mul(contradCoeffSmaller,contradRightBigger))),leq(mul(contradLeft,contradCoeffSmaller),contradRightSmaller))) 
\heuristics(notHumanReadable, inEqSimp_contradInEqs, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_exactShadow0

```
inEqSimp_exactShadow0 {
\assumes ([leq(mul(esLeft,esCoeff1),esRight1)]==>[]) 
\find(geq(mul(esLeft,esCoeff2),esRight2)==>)
\add [imp(and(gt(esCoeff1,Z(0(#))),gt(esCoeff2,Z(0(#)))),geq(add(mul(Z(neglit(1(#))),mul(esCoeff1,esRight2)),mul(esCoeff2,esRight1)),Z(0(#))))]==>[] 
\heuristics(notHumanReadable, inEqSimp_exactShadow, inEqSimp_saturate)
Choices: {}}
```

## inEqSimp_exactShadow1

```
inEqSimp_exactShadow1 {
\assumes ([leq(esLeft,esRight1)]==>[]) 
\find(geq(mul(esLeft,esCoeff2),esRight2)==>)
\add [imp(gt(esCoeff2,Z(0(#))),geq(add(mul(Z(neglit(1(#))),esRight2),mul(esCoeff2,esRight1)),Z(0(#))))]==>[] 
\heuristics(notHumanReadable, inEqSimp_exactShadow, inEqSimp_saturate)
Choices: {}}
```

## inEqSimp_exactShadow2

```
inEqSimp_exactShadow2 {
\assumes ([leq(mul(esLeft,esCoeff1),esRight1)]==>[]) 
\find(geq(esLeft,esRight2)==>)
\add [imp(gt(esCoeff1,Z(0(#))),geq(add(mul(Z(neglit(1(#))),mul(esCoeff1,esRight2)),esRight1),Z(0(#))))]==>[] 
\heuristics(notHumanReadable, inEqSimp_exactShadow, inEqSimp_saturate)
Choices: {}}
```

## inEqSimp_exactShadow3

```
inEqSimp_exactShadow3 {
\assumes ([leq(esLeft,esRight1)]==>[]) 
\find(geq(esLeft,esRight2)==>)
\add [geq(add(mul(Z(neglit(1(#))),esRight2),esRight1),Z(0(#)))]==>[] 
\heuristics(notHumanReadable, inEqSimp_exactShadow, inEqSimp_saturate)
Choices: {}}
```

## inEqSimp_geqRight

```
inEqSimp_geqRight {
\find(==>geq(i,i0))
\replacewith([leq(add(add(Z(1(#)),mul(Z(neglit(1(#))),i0)),i),Z(0(#)))]==>[]) 
\heuristics(notHumanReadable, inEqSimp_moveLeft, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_gtRight

```
inEqSimp_gtRight {
\find(==>gt(i,i0))
\replacewith([leq(add(mul(Z(neglit(1(#))),i0),i),Z(0(#)))]==>[]) 
\heuristics(notHumanReadable, inEqSimp_moveLeft, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_gtToGeq

```
inEqSimp_gtToGeq {
\find(gt(i,i0))
\replacewith(geq(add(add(Z(neglit(1(#))),mul(Z(neglit(1(#))),i0)),i),Z(0(#)))) 
\heuristics(notHumanReadable, inEqSimp_makeNonStrict, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_homoInEq0

```
inEqSimp_homoInEq0 {
\find(leq(homoLeft,homoRight))
\replacewith(geq(add(homoRight,mul(homoLeft,Z(neglit(1(#))))),Z(0(#)))) 
\heuristics(notHumanReadable, inEqSimp_homo, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_homoInEq1

```
inEqSimp_homoInEq1 {
\find(geq(homoLeft,homoRight))
\replacewith(leq(add(homoRight,mul(homoLeft,Z(neglit(1(#))))),Z(0(#)))) 
\heuristics(notHumanReadable, inEqSimp_homo, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_invertInEq0

```
inEqSimp_invertInEq0 {
\find(leq(invertLeft,invertRight))
\replacewith(geq(mul(invertLeft,Z(neglit(1(#)))),mul(invertRight,Z(neglit(1(#)))))) 
\heuristics(notHumanReadable, inEqSimp_normalise, inEqSimp_directInEquations)
Choices: {}}
```

## inEqSimp_invertInEq1

```
inEqSimp_invertInEq1 {
\find(geq(invertLeft,invertRight))
\replacewith(leq(mul(invertLeft,Z(neglit(1(#)))),mul(invertRight,Z(neglit(1(#)))))) 
\heuristics(notHumanReadable, inEqSimp_normalise, inEqSimp_directInEquations)
Choices: {}}
```

## inEqSimp_leqRight

```
inEqSimp_leqRight {
\find(==>leq(i,i0))
\replacewith([geq(add(add(Z(neglit(1(#))),mul(Z(neglit(1(#))),i0)),i),Z(0(#)))]==>[]) 
\heuristics(notHumanReadable, inEqSimp_moveLeft, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_ltRight

```
inEqSimp_ltRight {
\find(==>lt(i,i0))
\replacewith([geq(add(mul(Z(neglit(1(#))),i0),i),Z(0(#)))]==>[]) 
\heuristics(notHumanReadable, inEqSimp_moveLeft, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_ltToLeq

```
inEqSimp_ltToLeq {
\find(lt(i,i0))
\replacewith(leq(add(add(Z(1(#)),mul(Z(neglit(1(#))),i0)),i),Z(0(#)))) 
\heuristics(notHumanReadable, inEqSimp_makeNonStrict, inEqSimp_expand)
Choices: {}}
```

## inEqSimp_notGeq

```
inEqSimp_notGeq {
\find(not(geq(i,i0)))
\replacewith(leq(add(add(Z(1(#)),mul(Z(neglit(1(#))),i0)),i),Z(0(#)))) 
\heuristics(notHumanReadable, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_notLeq

```
inEqSimp_notLeq {
\find(not(leq(i,i0)))
\replacewith(geq(add(add(Z(neglit(1(#))),mul(Z(neglit(1(#))),i0)),i),Z(0(#)))) 
\heuristics(notHumanReadable, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_antiSymm0

```
inEqSimp_or_antiSymm0 {
\find(or(leq(antiSymmLeft,antiSymmRightSmaller),geq(antiSymmLeft,antiSymmRightBigger)))
\replacewith(if-then-else(equals(add(Z(2(#)),antiSymmRightSmaller),antiSymmRightBigger),not(equals(antiSymmLeft,add(Z(1(#)),antiSymmRightSmaller))),or(leq(antiSymmLeft,antiSymmRightSmaller),geq(antiSymmLeft,antiSymmRightBigger)))) 
\heuristics(notHumanReadable, inEqSimp_or_antiSymm, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_antiSymm1

```
inEqSimp_or_antiSymm1 {
\find(or(or(b,leq(antiSymmLeft,antiSymmRightSmaller)),geq(antiSymmLeft,antiSymmRightBigger)))
\replacewith(or(b,if-then-else(equals(add(Z(2(#)),antiSymmRightSmaller),antiSymmRightBigger),not(equals(antiSymmLeft,add(Z(1(#)),antiSymmRightSmaller))),or(leq(antiSymmLeft,antiSymmRightSmaller),geq(antiSymmLeft,antiSymmRightBigger))))) 
\heuristics(notHumanReadable, inEqSimp_or_antiSymm, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_subsumption0

```
inEqSimp_or_subsumption0 {
\find(or(leq(subsumLeft,subsumRightSmaller),leq(subsumLeft,subsumRightBigger)))
\replacewith(or(and(geq(subsumRightSmaller,subsumRightBigger),leq(subsumLeft,subsumRightSmaller)),leq(subsumLeft,subsumRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_subsumption1

```
inEqSimp_or_subsumption1 {
\find(or(or(b,leq(subsumLeft,subsumRightSmaller)),leq(subsumLeft,subsumRightBigger)))
\replacewith(or(or(b,and(geq(subsumRightSmaller,subsumRightBigger),leq(subsumLeft,subsumRightSmaller))),leq(subsumLeft,subsumRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_subsumption2

```
inEqSimp_or_subsumption2 {
\find(or(geq(subsumLeft,subsumRightSmaller),geq(subsumLeft,subsumRightBigger)))
\replacewith(or(geq(subsumLeft,subsumRightSmaller),and(geq(subsumRightSmaller,subsumRightBigger),geq(subsumLeft,subsumRightBigger)))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_subsumption3

```
inEqSimp_or_subsumption3 {
\find(or(or(b,geq(subsumLeft,subsumRightSmaller)),geq(subsumLeft,subsumRightBigger)))
\replacewith(or(or(b,geq(subsumLeft,subsumRightSmaller)),and(geq(subsumRightSmaller,subsumRightBigger),geq(subsumLeft,subsumRightBigger)))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_subsumption4

```
inEqSimp_or_subsumption4 {
\find(or(equals(subsumLeft,subsumRightSmaller),leq(subsumLeft,subsumRightBigger)))
\replacewith(or(and(gt(subsumRightSmaller,subsumRightBigger),equals(subsumLeft,subsumRightSmaller)),leq(subsumLeft,subsumRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_subsumption5

```
inEqSimp_or_subsumption5 {
\find(or(or(b,equals(subsumLeft,subsumRightSmaller)),leq(subsumLeft,subsumRightBigger)))
\replacewith(or(or(b,and(gt(subsumRightSmaller,subsumRightBigger),equals(subsumLeft,subsumRightSmaller))),leq(subsumLeft,subsumRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_subsumption6

```
inEqSimp_or_subsumption6 {
\find(or(geq(subsumLeft,subsumRightSmaller),equals(subsumLeft,subsumRightBigger)))
\replacewith(or(geq(subsumLeft,subsumRightSmaller),and(gt(subsumRightSmaller,subsumRightBigger),equals(subsumLeft,subsumRightBigger)))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_subsumption7

```
inEqSimp_or_subsumption7 {
\find(or(or(b,geq(subsumLeft,subsumRightSmaller)),equals(subsumLeft,subsumRightBigger)))
\replacewith(or(or(b,geq(subsumLeft,subsumRightSmaller)),and(gt(subsumRightSmaller,subsumRightBigger),equals(subsumLeft,subsumRightBigger)))) 
\heuristics(notHumanReadable, inEqSimp_andOr_subsumption, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_tautInEq0

```
inEqSimp_or_tautInEq0 {
\find(or(leq(tautLeft,tautRightBigger),geq(tautLeft,tautRightSmaller)))
\replacewith(or(or(leq(tautLeft,tautRightBigger),geq(tautLeft,tautRightSmaller)),geq(tautRightBigger,add(Z(neglit(1(#))),tautRightSmaller)))) 
\heuristics(notHumanReadable, inEqSimp_or_tautInEqs, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_tautInEq1

```
inEqSimp_or_tautInEq1 {
\find(or(or(b,leq(tautLeft,tautRightBigger)),geq(tautLeft,tautRightSmaller)))
\replacewith(or(or(or(b,leq(tautLeft,tautRightBigger)),geq(tautLeft,tautRightSmaller)),geq(tautRightBigger,add(Z(neglit(1(#))),tautRightSmaller)))) 
\heuristics(notHumanReadable, inEqSimp_or_tautInEqs, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_tautInEq2

```
inEqSimp_or_tautInEq2 {
\find(or(geq(tautLeft,tautRightSmaller),leq(tautLeft,tautRightBigger)))
\replacewith(or(or(geq(tautLeft,tautRightSmaller),leq(tautLeft,tautRightBigger)),geq(tautRightBigger,add(Z(neglit(1(#))),tautRightSmaller)))) 
\heuristics(notHumanReadable, inEqSimp_or_tautInEqs, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_tautInEq3

```
inEqSimp_or_tautInEq3 {
\find(or(or(b,geq(tautLeft,tautRightSmaller)),leq(tautLeft,tautRightBigger)))
\replacewith(or(or(or(b,geq(tautLeft,tautRightSmaller)),leq(tautLeft,tautRightBigger)),geq(tautRightBigger,add(Z(neglit(1(#))),tautRightSmaller)))) 
\heuristics(notHumanReadable, inEqSimp_or_tautInEqs, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_weaken0

```
inEqSimp_or_weaken0 {
\find(or(leq(weakenLeft,weakenRightSmaller),equals(weakenLeft,weakenRightBigger)))
\replacewith(if-then-else(equals(weakenRightBigger,add(Z(1(#)),weakenRightSmaller)),leq(weakenLeft,weakenRightBigger),or(leq(weakenLeft,weakenRightSmaller),equals(weakenLeft,weakenRightBigger)))) 
\heuristics(notHumanReadable, inEqSimp_or_weaken, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_weaken1

```
inEqSimp_or_weaken1 {
\find(or(equals(weakenLeft,weakenRightSmaller),geq(weakenLeft,weakenRightBigger)))
\replacewith(if-then-else(equals(weakenRightBigger,add(Z(1(#)),weakenRightSmaller)),geq(weakenLeft,weakenRightSmaller),or(equals(weakenLeft,weakenRightSmaller),geq(weakenLeft,weakenRightBigger)))) 
\heuristics(notHumanReadable, inEqSimp_or_weaken, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_weaken2

```
inEqSimp_or_weaken2 {
\find(or(or(b,leq(weakenLeft,weakenRightSmaller)),equals(weakenLeft,weakenRightBigger)))
\replacewith(or(b,if-then-else(equals(weakenRightBigger,add(Z(1(#)),weakenRightSmaller)),leq(weakenLeft,weakenRightBigger),or(leq(weakenLeft,weakenRightSmaller),equals(weakenLeft,weakenRightBigger))))) 
\heuristics(notHumanReadable, inEqSimp_or_weaken, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_or_weaken3

```
inEqSimp_or_weaken3 {
\find(or(or(b,equals(weakenLeft,weakenRightSmaller)),geq(weakenLeft,weakenRightBigger)))
\replacewith(or(b,if-then-else(equals(weakenRightBigger,add(Z(1(#)),weakenRightSmaller)),geq(weakenLeft,weakenRightSmaller),or(equals(weakenLeft,weakenRightSmaller),geq(weakenLeft,weakenRightBigger))))) 
\heuristics(notHumanReadable, inEqSimp_or_weaken, inEqSimp_forNormalisation)
Choices: {}}
```

## inEqSimp_sepNegMonomial0

```
inEqSimp_sepNegMonomial0 {
\find(leq(add(sepResidue,sepNegMono),Z(0(#))))
\replacewith(geq(mul(sepNegMono,Z(neglit(1(#)))),sepResidue)) 
\heuristics(notHumanReadable, inEqSimp_balance, inEqSimp_directInEquations)
Choices: {}}
```

## inEqSimp_sepNegMonomial1

```
inEqSimp_sepNegMonomial1 {
\find(geq(add(sepResidue,sepNegMono),Z(0(#))))
\replacewith(leq(mul(sepNegMono,Z(neglit(1(#)))),sepResidue)) 
\heuristics(notHumanReadable, inEqSimp_balance, inEqSimp_directInEquations)
Choices: {}}
```

## inEqSimp_sepPosMonomial0

```
inEqSimp_sepPosMonomial0 {
\find(leq(add(sepResidue,sepPosMono),Z(0(#))))
\replacewith(leq(sepPosMono,mul(sepResidue,Z(neglit(1(#)))))) 
\heuristics(notHumanReadable, inEqSimp_balance, inEqSimp_directInEquations)
Choices: {}}
```

## inEqSimp_sepPosMonomial1

```
inEqSimp_sepPosMonomial1 {
\find(geq(add(sepResidue,sepPosMono),Z(0(#))))
\replacewith(geq(sepPosMono,mul(sepResidue,Z(neglit(1(#)))))) 
\heuristics(notHumanReadable, inEqSimp_balance, inEqSimp_directInEquations)
Choices: {}}
```

## inEqSimp_strengthen0

```
inEqSimp_strengthen0 {
\assumes ([]==>[equals(strengthenLeft,strengthenRight)]) 
\find(leq(strengthenLeft,strengthenRight)==>)
\replacewith([leq(strengthenLeft,add(Z(neglit(1(#))),strengthenRight))]==>[]) 
\heuristics(notHumanReadable, inEqSimp_strengthen, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_strengthen1

```
inEqSimp_strengthen1 {
\assumes ([]==>[equals(strengthenLeft,strengthenRight)]) 
\find(geq(strengthenLeft,strengthenRight)==>)
\replacewith([geq(strengthenLeft,add(Z(1(#)),strengthenRight))]==>[]) 
\heuristics(notHumanReadable, inEqSimp_strengthen, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_subsumption0

```
inEqSimp_subsumption0 {
\assumes ([leq(subsumLeft,subsumRightSmaller)]==>[]) 
\find(leq(subsumLeft,subsumRightBigger))
\sameUpdateLevel\replacewith(or(leq(subsumRightSmaller,subsumRightBigger),leq(subsumLeft,subsumRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_subsumption, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_subsumption1

```
inEqSimp_subsumption1 {
\assumes ([geq(subsumLeft,subsumRightBigger)]==>[]) 
\find(geq(subsumLeft,subsumRightSmaller))
\sameUpdateLevel\replacewith(or(leq(subsumRightSmaller,subsumRightBigger),geq(subsumLeft,subsumRightSmaller))) 
\heuristics(notHumanReadable, inEqSimp_subsumption, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_subsumption2

```
inEqSimp_subsumption2 {
\assumes ([leq(mul(subsumLeft,subsumCoeffSmaller),subsumRightSmaller)]==>[]) 
\find(leq(mul(subsumLeft,subsumCoeffBigger),subsumRightBigger))
\sameUpdateLevel\replacewith(or(and(and(gt(subsumCoeffSmaller,Z(0(#))),gt(subsumCoeffBigger,Z(0(#)))),leq(mul(subsumCoeffBigger,subsumRightSmaller),mul(subsumCoeffSmaller,subsumRightBigger))),leq(mul(subsumLeft,subsumCoeffBigger),subsumRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_subsumption, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_subsumption4

```
inEqSimp_subsumption4 {
\assumes ([leq(subsumLeft,subsumRightSmaller)]==>[]) 
\find(leq(mul(subsumLeft,subsumCoeffBigger),subsumRightBigger))
\sameUpdateLevel\replacewith(or(and(gt(subsumCoeffBigger,Z(0(#))),leq(mul(subsumCoeffBigger,subsumRightSmaller),subsumRightBigger)),leq(mul(subsumLeft,subsumCoeffBigger),subsumRightBigger))) 
\heuristics(notHumanReadable, inEqSimp_subsumption, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_subsumption5

```
inEqSimp_subsumption5 {
\assumes ([geq(mul(subsumLeft,subsumCoeffBigger),subsumRightBigger)]==>[]) 
\find(geq(mul(subsumLeft,subsumCoeffSmaller),subsumRightSmaller))
\sameUpdateLevel\replacewith(or(and(and(gt(subsumCoeffSmaller,Z(0(#))),gt(subsumCoeffBigger,Z(0(#)))),leq(mul(subsumCoeffBigger,subsumRightSmaller),mul(subsumCoeffSmaller,subsumRightBigger))),geq(mul(subsumLeft,subsumCoeffSmaller),subsumRightSmaller))) 
\heuristics(notHumanReadable, inEqSimp_subsumption, inEqSimp_propagation)
Choices: {}}
```

## inEqSimp_subsumption6

```
inEqSimp_subsumption6 {
\assumes ([geq(subsumLeft,subsumRightBigger)]==>[]) 
\find(geq(mul(subsumLeft,subsumCoeffSmaller),subsumRightSmaller))
\sameUpdateLevel\replacewith(or(and(gt(subsumCoeffSmaller,Z(0(#))),leq(subsumRightSmaller,mul(subsumCoeffSmaller,subsumRightBigger))),geq(mul(subsumLeft,subsumCoeffSmaller),subsumRightSmaller))) 
\heuristics(notHumanReadable, inEqSimp_subsumption, inEqSimp_propagation)
Choices: {}}
```

## inInt

```
inInt {
\find(inInt(i))
\replacewith(true) 
\heuristics(concrete)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## inLong

```
inLong {
\find(inLong(i))
\replacewith(true) 
\heuristics(concrete)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## inShort

```
inShort {
\find(inShort(i))
\replacewith(true) 
\heuristics(concrete)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## indexOf

```
indexOf {
\find(clIndexOfChar(l,c,i))
\varcond(\notFreeIn(iv (variable), i (int term)), \notFreeIn(iv (variable), c (int term)), \notFreeIn(iv (variable), l (Seq term)))
\replacewith(ifExThenElse{iv (variable)}(and(and(and(geq(i,Z(0(#))),geq(iv,i)),lt(iv,seqLen(l))),equals(int::seqGet(l,iv),c)),iv,Z(neglit(1(#))))) 
\heuristics(stringsExpandDefNormalOp)
Choices: {Strings:on}}
```

## indexOfSeqConcatFirst

```
indexOfSeqConcatFirst {
\find(seqIndexOf(seqConcat(s1,s2),x))
\sameUpdateLevel\varcond(\notFreeIn(idx (variable), x (any term)), \notFreeIn(idx (variable), s2 (Seq term)), \notFreeIn(idx (variable), s1 (Seq term)))
\add []==>[exists{idx (variable)}(and(and(leq(Z(0(#)),idx),lt(idx,seqLen(s1))),equals(any::seqGet(s1,idx),x)))] ;
\replacewith(seqIndexOf(s1,x)) 

Choices: {sequences:on}}
```

## indexOfSeqConcatSecond

```
indexOfSeqConcatSecond {
\find(seqIndexOf(seqConcat(s1,s2),x))
\sameUpdateLevel\varcond(\notFreeIn(idx (variable), x (any term)), \notFreeIn(idx (variable), s2 (Seq term)), \notFreeIn(idx (variable), s1 (Seq term)))
\add []==>[and(not(exists{idx (variable)}(and(and(leq(Z(0(#)),idx),lt(idx,seqLen(s1))),equals(any::seqGet(s1,idx),x)))),exists{idx (variable)}(and(and(leq(Z(0(#)),idx),lt(idx,seqLen(s2))),equals(any::seqGet(s2,idx),x))))] ;
\replacewith(add(seqIndexOf(s2,x),seqLen(s1))) 

Choices: {sequences:on}}
```

## indexOfSeqSingleton

```
indexOfSeqSingleton {
\find(seqIndexOf(seqSingleton(x),x))
\sameUpdateLevel\replacewith(Z(0(#))) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## indexOfSeqSub

```
indexOfSeqSub {
\find(seqIndexOf(seqSub(s,from,to),x))
\sameUpdateLevel\varcond(\notFreeIn(nx (variable), to (int term)), \notFreeIn(nx (variable), from (int term)), \notFreeIn(nx (variable), x (any term)), \notFreeIn(nx (variable), s (Seq term)))
\add []==>[and(and(and(leq(from,seqIndexOf(s,x)),lt(seqIndexOf(s,x),to)),leq(Z(0(#)),from)),exists{nx (variable)}(and(and(leq(Z(0(#)),nx),lt(nx,seqLen(s))),equals(any::seqGet(s,nx),x))))] ;
\replacewith(sub(seqIndexOf(s,x),from)) 

Choices: {sequences:on}}
```

## indexOfStr

```
indexOfStr {
\find(clIndexOfCl(sourceStr,i,searchStr))
\varcond(\notFreeIn(iv (variable), i (int term)), \notFreeIn(iv (variable), sourceStr (Seq term)), \notFreeIn(iv (variable), searchStr (Seq term)))
\replacewith(ifExThenElse{iv (variable)}(and(and(and(geq(iv,i),geq(iv,Z(0(#)))),leq(add(iv,seqLen(searchStr)),seqLen(sourceStr))),equals(seqSub(sourceStr,iv,add(iv,seqLen(searchStr))),searchStr)),iv,Z(neglit(1(#))))) 
\heuristics(stringsExpandDefNormalOp)
Choices: {Strings:on}}
```

## ineffectiveCast

```
ineffectiveCast {
\assumes ([equals(H::instance(t),TRUE)]==>[]) 
\find(H::cast(t))
\sameUpdateLevel\add [equals(H::cast(t),t)]==>[] 
\heuristics(inReachableStateImplication)
Choices: {}}
```

## ineffectiveCast2

```
ineffectiveCast2 {
\assumes ([equals(cs,gt)]==>[]) 
\find(C::cast(gt))
\sameUpdateLevel\add [equals(C::cast(gt),gt)]==>[] 
\heuristics(inReachableStateImplication)
Choices: {}}
```

## ineffectiveCast3

```
ineffectiveCast3 {
\assumes ([equals(H::exactInstance(t),TRUE)]==>[]) 
\find(H::cast(t))
\sameUpdateLevel\add [equals(H::cast(t),t)]==>[] 
\heuristics(inReachableStateImplication)
Choices: {}}
```

## inequality

```
compound_inequality_comparison_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse0!=#se; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse0 (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse0) #v0 = #nse0;#lhs=#v0!=#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## inequality

```
compound_inequality_comparison_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e!=#nse0; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse0 (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse0) #v1 = #nse0;#lhs=#v0!=#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## inequality comparison

```
inequality_comparison_new {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0!=#se1; ... }\] (post))
\replacewith(if-then-else(not(equals(#se0,#se1)),#allmodal ( (modal operator))\[{ .. #lhs=true; ... }\] (post),#allmodal ( (modal operator))\[{ .. #lhs=false; ... }\] (post))) 
\heuristics(split_if, simplify_prog, obsolete)
Choices: {programRules:Java}}
```

## inequality comparison

```
inequality_comparison_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0!=#se1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(equals(#se0,#se1),FALSE,TRUE)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## infiniteUnionUnused

```
infiniteUnionUnused {
\find(infiniteUnion{av (variable)}(s))
\varcond(\notFreeIn(av (variable), s (LocSet term)))
\replacewith(s) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## initFullPermission

```
initFullPermission {
\find(initFullPermission)
\replacewith(slice(consPermissionOwnerList(currentThread,emptyPermissionOwnerList),emptyPermission)) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## initialisation process has already terminated

```
class_initialized_excludes_class_init_in_progress {
\assumes ([equals(boolean::select(heap,null,alphaObj::<classInitialized>),TRUE),wellFormed(heap)]==>[]) 
\find(boolean::select(heap,null,alphaObj::<classInitializationInProgress>))
\sameUpdateLevel\replacewith(FALSE) 
\heuristics(simplify, confluence_restricted)
Choices: {programRules:Java}}
```

## initialization process has already terminated (or never begun)

```
class_erroneous_excludes_class_in_init {
\assumes ([equals(boolean::select(heap,null,alphaObj::<classErroneous>),TRUE),wellFormed(heap)]==>[]) 
\find(boolean::select(heap,null,alphaObj::<classInitializationInProgress>))
\sameUpdateLevel\replacewith(FALSE) 
\heuristics(simplify, confluence_restricted)
Choices: {programRules:Java}}
```

## initialized class is not erroneous

```
initialized_class_is_not_erroneous {
\assumes ([equals(boolean::select(heap,null,alphaObj::<classInitialized>),TRUE),wellFormed(heap)]==>[]) 
\find(boolean::select(heap,null,alphaObj::<classErroneous>))
\sameUpdateLevel\replacewith(FALSE) 
\heuristics(simplify, confluence_restricted)
Choices: {programRules:Java}}
```

## initialized classes have been prepared

```
class_being_initialized_is_prepared {
\assumes ([equals(boolean::select(heap,null,alphaObj::<classInitializationInProgress>),TRUE),wellFormed(heap)]==>[]) 
\find(boolean::select(heap,null,alphaObj::<classPrepared>))
\sameUpdateLevel\replacewith(TRUE) 
\heuristics(simplify, confluence_restricted)
Choices: {programRules:Java}}
```

## insertPermissionOwner

```
insertPermissionOwner {
\find(insertPermissionOwner(o,no,depth,consPermissionOwnerList(owner,ol)))
\add []==>[and(geq(depth,Z(0(#))),checkPermissionOwner(o,depth,consPermissionOwnerList(owner,ol)))] ;
\replacewith(if-then-else(equals(depth,Z(0(#))),consPermissionOwnerList(no,consPermissionOwnerList(owner,ol)),consPermissionOwnerList(owner,insertPermissionOwner(o,no,sub(depth,Z(1(#))),ol)))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## insert_constant_string_value

```
insert_constant_string_value {
\assumes ([wellFormed(heap)]==>[]) 
\find(#csv)
\sameUpdateLevel\replacewith(if-then-else(equals(#constantvalue(#csv),null),null,strPool(Seq::cast(#constantvalue(#csv))))) 
\heuristics(concrete)
Choices: {}}
```

## insert_constant_value

```
insert_constant_value {
\find(#cv)
\replacewith(#constantvalue(#cv)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## insert_eq_all

```
insert_eq_all {
\find(equals(sr,tr)==>)
\addrules [auto_insert_eq {
\find(sr)
\replacewith(tr) 
\heuristics(simplify)
Choices: {}}] \replacewith([]==>[]) 

Choices: {}}
```

## insert_eqv_lr

```
insert_eqv_lr {
\find(equiv(br,cr)==>)
\addrules [insert_eqv {
\find(br)
\replacewith(cr) 
\heuristics(simplify)
Choices: {}}] 

Choices: {}}
```

## insert_eqv_once_lr

```
insert_eqv_once_lr {
\find(equiv(br,cr)==>)
\addrules [insert_eqv {
\find(br)
\replacewith(cr) 

Choices: {}}] 

Choices: {}}
```

## insert_eqv_once_rl

```
insert_eqv_once_rl {
\find(equiv(br,cr)==>)
\addrules [insert_eqv {
\find(cr)
\replacewith(br) 

Choices: {}}] 

Choices: {}}
```

## insert_eqv_rl

```
insert_eqv_rl {
\find(equiv(br,cr)==>)
\addrules [insert_eqv {
\find(cr)
\replacewith(br) 
\heuristics(simplify)
Choices: {}}] 

Choices: {}}
```

## instAll

```
instAll {
\assumes ([all{u (variable)}(b)]==>[]) 
\find(t)
\add [subst{u (variable)}(t,b)]==>[] 

Choices: {}}
```

## instEx

```
instEx {
\assumes ([]==>[exists{u (variable)}(b)]) 
\find(t)
\add []==>[subst{u (variable)}(t,b)] 

Choices: {}}
```

## instanceCreation

```
instanceCreation {
\find(#allmodal ( (modal operator))\[{ .. #n ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#n (program SimpleInstanceCreation))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#v0) #v0 = create-object(#n);constructor-call(#n)post-work(#v0) ... }\] (post)) 
\heuristics(method_expand)
Choices: {programRules:Java}}
```

## instanceCreationAssignment

```
instanceCreationAssignment {
\find(#normal ( (modal operator))\[{ .. #lhs=#n; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))))
\replacewith(#normal ( (modal operator))\[{ .. #typeof(#v0) #v0 = create-object(#n);constructor-call(#n)post-work(#v0)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {programRules:Java}}
```

## instanceCreationAssignmentUnfoldArguments

```
instanceCreationAssignmentUnfoldArguments {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nsn; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #evaluate-arguments(#lhs=#nsn;) ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## instanceCreationUnfoldArguments

```
instanceCreationUnfoldArguments {
\find(#allmodal ( (modal operator))\[{ .. #nsn ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #evaluate-arguments(#nsn) ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## instanceof disjoint type

```
instanceof_not_compatible {
\find(equals(G::instance(a),TRUE))
\varcond(\sub(Null, G), \disjointModuloNull(G, \typeof(a (any term))), )
\replacewith(equals(a,null)) 
\heuristics(evaluate_instanceof, concrete)
Choices: {}}
```

## instanceof disjoint type

```
instanceof_not_compatible_2 {
\find(equals(G::instance(a),FALSE))
\varcond(\sub(Null, G), \disjointModuloNull(G, \typeof(a (any term))), )
\replacewith(not(equals(a,null))) 
\heuristics(evaluate_instanceof, concrete)
Choices: {}}
```

## instanceof disjoint type

```
instanceof_not_compatible_3 {
\find(equals(G::instance(a),TRUE))
\varcond(\not\sub(Null, G), \disjointModuloNull(G, \typeof(a (any term))), )
\replacewith(false) 
\heuristics(evaluate_instanceof, concrete)
Choices: {}}
```

## instanceof disjoint type

```
instanceof_not_compatible_4 {
\find(equals(G::instance(a),FALSE))
\varcond(\not\sub(Null, G), \disjointModuloNull(G, \typeof(a (any term))), )
\replacewith(true) 
\heuristics(evaluate_instanceof, concrete)
Choices: {}}
```

## instanceof disjoint type

```
instanceof_not_compatible_5 {
\assumes ([equals(H::instance(a),TRUE)]==>[]) 
\find(equals(G::instance(a),TRUE))
\varcond(\sub(Null, G), \disjointModuloNull(G, H), )
\replacewith(equals(a,null)) 
\heuristics(evaluate_instanceof, concrete)
Choices: {}}
```

## instanceof static supertype

```
instanceof_static_type {
\find(G::instance(a))
\varcond(\sub(\typeof(a (any term)), G), )
\replacewith(TRUE) 
\heuristics(evaluate_instanceof, concrete)
Choices: {}}
```

## instanceof static supertype

```
instanceof_static_type_2 {
\assumes ([equals(a2,a)]==>[]) 
\find(G::instance(a))
\sameUpdateLevel\varcond(\sub(\typeof(a2 (any term)), G), )
\replacewith(TRUE) 
\heuristics(evaluate_instanceof, concrete)
Choices: {}}
```

## instanceof_eval

```
instanceof_eval {
\find(#allmodal ( (modal operator))\[{ .. #v=#nse instanceof #t; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v0 = #nse;#v=#v0 instanceof #t; ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## instanceof_known_dynamic_type

```
instanceof_known_dynamic_type {
\assumes ([equals(G::exactInstance(a),TRUE)]==>[]) 
\find(H::instance(a))
\sameUpdateLevel\varcond(\sub(G, H), )
\replacewith(TRUE) 
\heuristics(evaluate_instanceof, simplify)
Choices: {}}
```

## instanceof_known_dynamic_type_2

```
instanceof_known_dynamic_type_2 {
\assumes ([equals(G::exactInstance(a),TRUE)]==>[]) 
\find(H::instance(a))
\sameUpdateLevel\varcond(\not\sub(G, H), )
\replacewith(FALSE) 
\heuristics(evaluate_instanceof, simplify)
Choices: {}}
```

## int_diff_minus_eq

```
int_diff_minus_eq {
\find(sub(i0,neg(i1)))
\replacewith(add(i0,i1)) 

Choices: {}}
```

## int_induction

```
int_induction {
\add [all{nv (variable)}(imp(geq(nv,Z(0(#))),b))]==>[] ;
\add []==>[all{nv (variable)}(imp(and(geq(nv,Z(0(#))),b),subst{nv (variable)}(add(nv,Z(1(#))),b)))] ;
\add []==>[subst{nv (variable)}(Z(0(#)),b)] 

Choices: {}}
```

## interfaces or abstract classes have no exact instances

```
exact_instance_for_interfaces_or_abstract_classes {
\find(G::exactInstance(obj))
\varcond(\isAbstractOrInterface (G), )
\replacewith(FALSE) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## intersectAllFieldsFreshLocs

```
intersectAllFieldsFreshLocs {
\find(equals(intersect(allFields(o),freshLocs(h)),empty))
\replacewith(or(equals(o,null),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## intersectWithAllLocs

```
intersectWithAllLocs {
\find(intersect(allLocs,s))
\replacewith(s) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## intersectWithAllLocs

```
intersectWithAllLocsRight {
\find(intersect(s,allLocs))
\replacewith(s) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## intersectWithEmpty

```
intersectWithEmpty {
\find(intersect(empty,s))
\replacewith(empty) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## intersectWithEmpty

```
intersectWithEmptyRight {
\find(intersect(s,empty))
\replacewith(empty) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## intersectWithItself

```
intersectWithItself {
\find(intersect(s,s))
\replacewith(s) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## intersectWithSingleton

```
intersectWithSingleton {
\find(intersect(singleton(o,f),s))
\replacewith(if-then-else(elementOf(o,f,s),singleton(o,f),empty)) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## intersectionSetMinusItself

```
intersectionSetMinusItself {
\find(intersect(setMinus(s1,s2),s2))
\replacewith(empty) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## intersectionSetMinusItself_2

```
intersectionSetMinusItself_2 {
\find(intersect(s2,setMinus(s1,s2)))
\replacewith(empty) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## invertBits

```
bitwiseNegation {
\find(#normalassign ( (modal operator))\[{ .. #loc=~#se; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaBitwiseNegation(#se)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## invert_bits

```
compound_invert_bits {
\find(#allmodal ( (modal operator))\[{ .. #lhs=~#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v1 = #nse;#lhs=~#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## isFiniteOfMapEmpty

```
isFiniteOfMapEmpty {
\find(isFinite(mapEmpty))
\sameUpdateLevel\replacewith(true) 
\heuristics(simplify)
Choices: {}}
```

## isFiniteOfMapRemove

```
isFiniteOfMapRemove {
\find(isFinite(mapRemove(m,key)))
\sameUpdateLevel\replacewith(isFinite(m)) 
\heuristics(simplify)
Choices: {}}
```

## isFiniteOfMapSingleton

```
isFiniteOfMapSingleton {
\find(isFinite(mapSingleton(key,value)))
\sameUpdateLevel\replacewith(true) 
\heuristics(simplify)
Choices: {}}
```

## isFiniteOfMapUpdate

```
isFiniteOfMapUpdate {
\find(isFinite(mapUpdate(m,key,value)))
\sameUpdateLevel\replacewith(isFinite(m)) 
\heuristics(simplify)
Choices: {}}
```

## isFiniteOfSeq2Map

```
isFiniteOfSeq2Map {
\find(isFinite(seq2map(s)))
\sameUpdateLevel\replacewith(true) 
\heuristics(simplify)
Choices: {}}
```

## javaShiftLeftIntDef

```
javaShiftLeftIntDef {
\find(shiftleftJint(left,right))
\replacewith(moduloInt(shiftleft(left,mod(right,Z(2(3(#))))))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## javaShiftLeftLongDef

```
javaShiftLeftLongDef {
\find(shiftleftJlong(left,right))
\replacewith(moduloLong(shiftleft(left,mod(right,Z(4(6(#))))))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## javaShiftRightIntDef

```
javaShiftRightIntDef {
\find(shiftrightJint(left,right))
\replacewith(moduloInt(shiftright(left,mod(right,Z(2(3(#))))))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## javaShiftRightLongDef

```
javaShiftRightLongDef {
\find(shiftrightJlong(left,right))
\replacewith(moduloLong(shiftright(left,mod(right,Z(4(6(#))))))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## jdiv_axiom

```
jdiv_axiom {
\find(jdiv(divNum,divDenom))
\sameUpdateLevel\add [equals(jdiv(divNum,divDenom),if-then-else(geq(divNum,Z(0(#))),div(divNum,divDenom),mul(div(mul(divNum,Z(neglit(1(#)))),divDenom),Z(neglit(1(#))))))]==>[] 
\heuristics(notHumanReadable, defOps_jdiv)
Choices: {integerSimplificationRules:full}}
```

## jdiv_axiom_inline

```
jdiv_axiom_inline {
\find(jdiv(divNum,divDenom))
\replacewith(if-then-else(geq(divNum,Z(0(#))),div(divNum,divDenom),mul(div(mul(divNum,Z(neglit(1(#)))),divDenom),Z(neglit(1(#)))))) 
\heuristics(notHumanReadable, defOps_jdiv_inline)
Choices: {integerSimplificationRules:full}}
```

## jmod_axiom

```
jmod_axiom {
\find(jmod(divNum,divDenom))
\replacewith(add(divNum,mul(mul(jdiv(divNum,divDenom),Z(neglit(1(#)))),divDenom))) 
\heuristics(notHumanReadable, defOps_mod)
Choices: {integerSimplificationRules:full}}
```

## known dynamic array type

```
array_store_known_dynamic_array_type {
\assumes ([equals(J::exactInstance(array),TRUE)]==>[]) 
\find(arrayStoreValid(array,obj))
\sameUpdateLevel\varcond(\isReference[non_null]( J ), )
\replacewith(or(equals(obj,null),equals(#arrayBaseInstanceOf(J::exactInstance(array),obj),TRUE))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## lastIndexOf

```
lastIndexOf {
\find(clLastIndexOfChar(sourceStr,c,i))
\varcond(\notFreeIn(iv (variable), sourceStr (Seq term)), \notFreeIn(iv (variable), i (int term)), \notFreeIn(iv (variable), c (int term)))
\replacewith(ifExThenElse{iv (variable)}(and(and(and(gt(iv,Z(0(#))),geq(i,iv)),lt(sub(i,iv),seqLen(sourceStr))),equals(int::seqGet(sourceStr,sub(i,iv)),c)),sub(i,iv),Z(neglit(1(#))))) 
\heuristics(stringsExpandDefNormalOp)
Choices: {Strings:on}}
```

## lastIndexOfStr

```
lastIndexOfStr {
\find(clLastIndexOfCl(sourceStr,i,searchStr))
\varcond(\notFreeIn(iv (variable), sourceStr (Seq term)), \notFreeIn(iv (variable), i (int term)), \notFreeIn(iv (variable), searchStr (Seq term)))
\replacewith(ifExThenElse{iv (variable)}(and(and(and(gt(iv,Z(0(#))),geq(sub(i,iv),Z(0(#)))),leq(sub(add(seqLen(searchStr),i),iv),seqLen(sourceStr))),equals(seqSub(sourceStr,sub(i,iv),sub(add(seqLen(searchStr),i),iv)),searchStr)),sub(i,iv),Z(neglit(1(#))))) 
\heuristics(stringsExpandDefNormalOp)
Choices: {Strings:on}}
```

## le1_add1_eq_le

```
le1_add1_eq_le {
\find(lt(i0,add(i1,Z(1(#)))))
\replacewith(leq(i0,i1)) 

Choices: {}}
```

## left_add_mult_distrib

```
left_add_mult_distrib {
\find(add(mul(i0,i1),add(mul(i2,i1),i3)))
\replacewith(add(mul(add(i0,i2),i1),i3)) 

Choices: {}}
```

## lenNonNegative

```
lenNonNegative {
\find(seqLen(seq))
\sameUpdateLevel\add [leq(Z(0(#)),seqLen(seq))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {sequences:on}}
```

## lenOfArray2seq

```
lenOfArray2seq {
\find(seqLen(array2seq(h,a)))
\replacewith(length(a)) 

Choices: {sequences:on}}
```

## lenOfNPermInv

```
lenOfNPermInv {
\find(seqLen(seqNPermInv(s1)))
\replacewith(seqLen(s1)) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## lenOfRemove

```
lenOfRemove {
\find(seqLen(seqRemove(s1,iv1)))
\replacewith(if-then-else(and(leq(Z(0(#)),iv1),lt(iv1,seqLen(s1))),sub(seqLen(s1),Z(1(#))),seqLen(s1))) 
\heuristics(simplify_enlarging)
Choices: {moreSeqRules:on,sequences:on}}
```

## lenOfRemoveConcrete1

```
lenOfRemoveConcrete1 {
\assumes ([geq(seqLen(s1),Z(1(#)))]==>[]) 
\find(seqLen(seqRemove(s1,sub(seqLen(s1),Z(1(#))))))
\replacewith(sub(seqLen(s1),Z(1(#)))) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## lenOfRemoveConcrete2

```
lenOfRemoveConcrete2 {
\assumes ([geq(seqLen(s1),Z(1(#)))]==>[]) 
\find(seqLen(seqRemove(s1,Z(0(#)))))
\replacewith(sub(seqLen(s1),Z(1(#)))) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## lenOfSeqConcat

```
lenOfSeqConcat {
\find(seqLen(seqConcat(seq,seq2)))
\replacewith(add(seqLen(seq),seqLen(seq2))) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## lenOfSeqConcat

```
lenOfSeqConcatEQ {
\assumes ([equals(seqConcat(seq,seq2),EQ)]==>[]) 
\find(seqLen(EQ))
\sameUpdateLevel\replacewith(add(seqLen(seq),seqLen(seq2))) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## lenOfSeqDef

```
lenOfSeqDef {
\find(seqLen(seqDef{uSub (variable)}(from,to,t)))
\replacewith(if-then-else(lt(from,to),sub(to,from),Z(0(#)))) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## lenOfSeqDef

```
lenOfSeqDefEQ {
\assumes ([equals(seqDef{uSub (variable)}(from,to,t),EQ)]==>[]) 
\find(seqLen(EQ))
\sameUpdateLevel\replacewith(if-then-else(leq(from,to),sub(to,from),Z(0(#)))) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## lenOfSeqEmpty

```
lenOfSeqEmpty {
\find(seqLen(seqEmpty))
\replacewith(Z(0(#))) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## lenOfSeqEmpty

```
lenOfSeqEmptyEQ {
\assumes ([equals(seqEmpty,EQ)]==>[]) 
\find(seqLen(EQ))
\sameUpdateLevel\replacewith(Z(0(#))) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## lenOfSeqReverse

```
lenOfSeqReverse {
\find(seqLen(seqReverse(seq)))
\replacewith(seqLen(seq)) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## lenOfSeqReverse

```
lenOfSeqReverseEQ {
\assumes ([equals(seqReverse(seq),EQ)]==>[]) 
\find(seqLen(EQ))
\sameUpdateLevel\replacewith(seqLen(seq)) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## lenOfSeqSingleton

```
lenOfSeqSingleton {
\find(seqLen(seqSingleton(x)))
\replacewith(Z(1(#))) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## lenOfSeqSingleton

```
lenOfSeqSingletonEQ {
\assumes ([equals(seqSingleton(x),EQ)]==>[]) 
\find(seqLen(EQ))
\sameUpdateLevel\replacewith(Z(1(#))) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## lenOfSeqSub

```
lenOfSeqSub {
\find(seqLen(seqSub(seq,from,to)))
\replacewith(if-then-else(lt(from,to),sub(to,from),Z(0(#)))) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## lenOfSeqSub

```
lenOfSeqSubEQ {
\assumes ([equals(seqSub(seq,from,to),EQ)]==>[]) 
\find(seqLen(EQ))
\sameUpdateLevel\replacewith(if-then-else(lt(from,to),sub(to,from),Z(0(#)))) 
\heuristics(find_term_not_in_assumes, simplify)
Choices: {sequences:on}}
```

## lenOfSwap

```
lenOfSwap {
\find(seqLen(seqSwap(s1,iv1,iv2)))
\replacewith(seqLen(s1)) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## lengthReplace

```
lengthReplace {
\find(seqLen(clReplace(str,searchChar,replaceChar)))
\replacewith(seqLen(str)) 
\heuristics(stringsSimplify)
Choices: {Strings:on}}
```

## lengthReplaceEQ

```
lengthReplaceEQ {
\assumes ([equals(clReplace(str,searchChar,replaceChar),newStr)]==>[]) 
\find(seqLen(newStr))
\sameUpdateLevel\replacewith(seqLen(str)) 
\heuristics(stringsSimplify)
Choices: {Strings:on}}
```

## leq_add

```
leq_add {
\find(==>leq(i0,i1))
\varcond(\notFreeIn(j2 (variable), i1 (int term)), \notFreeIn(j2 (variable), i0 (int term)))
\replacewith([]==>[exists{j2 (variable)}(leq(add(i0,j2),add(i1,j2)))]) 

Choices: {}}
```

## leq_add_iff1

```
leq_add_iff1 {
\find(leq(add(mul(i0,i1),i2),add(mul(i3,i1),i4)))
\replacewith(leq(add(mul(sub(i0,i3),i1),i2),i4)) 

Choices: {}}
```

## leq_add_iff2

```
leq_add_iff2 {
\find(leq(add(mul(i0,i1),i2),add(mul(i3,i1),i4)))
\replacewith(leq(i2,add(mul(sub(i3,i0),i1),i4))) 

Choices: {}}
```

## leq_add_one

```
leq_add_one {
\find(leq(i0,i1))
\replacewith(leq(add(i0,Z(1(#))),add(i1,Z(1(#))))) 

Choices: {}}
```

## leq_diff1_eq

```
leq_diff1_eq {
\find(leq(i0,sub(i1,Z(1(#)))))
\replacewith(lt(i0,i1)) 

Choices: {}}
```

## leq_diff_1

```
leq_diff_1 {
\find(leq(i0,add(i0,Z(1(#)))))
\replacewith(true) 
\heuristics(int_arithmetic)
Choices: {}}
```

## leq_iff_diff_leq_0

```
leq_iff_diff_leq_0 {
\find(leq(i0,i1))
\replacewith(leq(sub(i0,i1),Z(0(#)))) 

Choices: {}}
```

## leq_literals

```
leq_literals {
\find(leq(Z(iz),Z(jz)))
\replacewith(#leq(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## leq_to_geq

```
leq_to_geq {
\find(leq(i,i0))
\replacewith(geq(i0,i)) 

Choices: {}}
```

## leq_to_gt

```
leq_to_gt {
\find(leq(i,j))
\replacewith(not(gt(i,j))) 

Choices: {}}
```

## leq_to_gt_alt

```
leq_to_gt_alt {
\find(leq(i,j))
\replacewith(or(lt(i,j),equals(i,j))) 

Choices: {}}
```

## leq_trans

```
leq_trans {
\assumes ([leq(i,i0)]==>[]) 
\find(leq(i0,i1)==>)
\add [leq(i,i1)]==>[] 

Choices: {}}
```

## less-or-equal than distinction

```
less_equal_than_comparison_new {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0<=#se1; ... }\] (post))
\replacewith(if-then-else(leq(#se0,#se1),#allmodal ( (modal operator))\[{ .. #lhs=true; ... }\] (post),#allmodal ( (modal operator))\[{ .. #lhs=false; ... }\] (post))) 
\heuristics(split_if, simplify_prog, obsolete)
Choices: {programRules:Java}}
```

## less-or-equal than distinction

```
less_equal_than_comparison_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0<=#se1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(leq(#se0,#se1),TRUE,FALSE)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## less_1_mult

```
less_1_mult {
\find(and(lt(Z(1(#)),i0),lt(Z(1(#)),i1)))
\replacewith(lt(Z(1(#)),mul(i0,i1))) 

Choices: {}}
```

## less_add

```
less_add {
\find(==>lt(i0,i1))
\varcond(\notFreeIn(j2 (variable), i1 (int term)), \notFreeIn(j2 (variable), i0 (int term)))
\replacewith([]==>[exists{j2 (variable)}(lt(add(i0,j2),add(i1,j2)))]) 

Choices: {}}
```

## less_add_iff1

```
less_add_iff1 {
\find(lt(add(mul(i0,i1),i2),add(mul(i3,i1),i4)))
\replacewith(lt(add(mul(sub(i0,i3),i1),i2),i4)) 

Choices: {}}
```

## less_add_iff2

```
less_add_iff2 {
\find(lt(add(mul(i0,i1),i2),add(mul(i3,i1),i4)))
\replacewith(lt(i2,add(mul(sub(i3,i0),i1),i4))) 

Choices: {}}
```

## less_add_one

```
less_add_one {
\find(lt(i0,i1))
\replacewith(lt(add(i0,Z(1(#))),add(i1,Z(1(#))))) 

Choices: {}}
```

## less_base

```
less_base {
\find(lt(i,i))
\replacewith(false) 

Choices: {}}
```

## less_iff_diff_less_0

```
less_iff_diff_less_0 {
\find(lt(i0,i1))
\replacewith(lt(sub(i0,i1),Z(0(#)))) 

Choices: {}}
```

## less_is_alternative_1

```
less_is_alternative_1 {
\assumes ([lt(i,i0),lt(i0,i)]==>[]) 
\closegoal
Choices: {}}
```

## less_is_alternative_2

```
less_is_alternative_2 {
\assumes ([]==>[lt(i,i0)]) 
\find(==>lt(i0,i))
\add [equals(i,i0)]==>[] 

Choices: {}}
```

## less_is_total

```
less_is_total {
\find(i)
\sameUpdateLevel\add [lt(i0,i)]==>[] ;
\add [equals(i,i0)]==>[] ;
\add [lt(i,i0)]==>[] 

Choices: {}}
```

## less_is_total_heu

```
less_is_total_heu {
\assumes ([]==>[lt(i,i0),equals(i,i0),lt(i0,i)]) 
\closegoal
Choices: {}}
```

## less_literals

```
less_literals {
\find(lt(Z(iz),Z(jz)))
\replacewith(#less(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## less_neg

```
less_neg {
\find(lt(i,i0))
\replacewith(not(lt(i0,add(i,Z(1(#)))))) 

Choices: {}}
```

## less_or_equal

```
compound_less_equal_than_comparison_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse0<=#se; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse0 (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse0) #v0 = #nse0;#lhs=#v0<=#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## less_or_equal

```
compound_less_equal_than_comparison_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e<=#nse0; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse0 (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse0) #v1 = #nse0;#lhs=#v0<=#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## less_plus

```
less_plus {
\find(lt(Z(0(#)),add(i0,i1)))
\replacewith(lt(neg(i0),i1)) 

Choices: {}}
```

## less_sub

```
less_sub {
\find(lt(i,i0))
\replacewith(lt(neg(i0),neg(i))) 

Choices: {}}
```

## less_than

```
compound_less_than_comparison_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse0<#se; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse0 (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse0) #v0 = #nse0;#lhs=#v0<#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## less_than

```
compound_less_than_comparison_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e<#nse0; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse0 (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse0) #v1 = #nse0;#lhs=#v0<#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## less_trans

```
less_trans {
\assumes ([lt(i,i0)]==>[]) 
\find(lt(i0,i1)==>)
\add [lt(i,i1)]==>[] 

Choices: {}}
```

## less_zero_is_total

```
less_zero_is_total {
\find(i)
\sameUpdateLevel\add [lt(Z(0(#)),i)]==>[] ;
\add [equals(i,Z(0(#)))]==>[] ;
\add [lt(i,Z(0(#)))]==>[] 

Choices: {}}
```

## lesser than distinction

```
less_than_comparison_new {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0<#se1; ... }\] (post))
\replacewith(if-then-else(lt(#se0,#se1),#allmodal ( (modal operator))\[{ .. #lhs=true; ... }\] (post),#allmodal ( (modal operator))\[{ .. #lhs=false; ... }\] (post))) 
\heuristics(split_if, simplify_prog, obsolete)
Choices: {programRules:Java}}
```

## lesser than distinction

```
less_than_comparison_simple {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se0<#se1; ... }\] (post))
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(if-then-else(lt(#se0,#se1),TRUE,FALSE)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## local_cut

```
local_cut {
\find(phi)
\replacewith(and(or(not(psi),phi),or(psi,phi))) 

Choices: {}}
```

## loopUnwind

```
loopUnwind {
\find(#allmodal ( (modal operator))\[{ .. while ( #e )
    #s
 ... }\] (post))
\varcond(\newLabel (#innerLabel (program Label)), \newLabel (#outerLabel (program Label)), )
\replacewith(#allmodal ( (modal operator))\[{ .. #unwind-loop(while ( #e )
      #s  ) ... }\] (post)) 
\heuristics(loop_expand)
Choices: {programRules:Java}}
```

## lt_diff_1

```
lt_diff_1 {
\find(lt(i0,add(i0,Z(1(#)))))
\replacewith(true) 
\heuristics(int_arithmetic)
Choices: {}}
```

## lt_to_gt

```
lt_to_gt {
\find(lt(i,i0))
\replacewith(gt(i0,i)) 

Choices: {}}
```

## lt_to_leq_1

```
lt_to_leq_1 {
\find(or(lt(i,j),equals(i,j)))
\replacewith(leq(i,j)) 

Choices: {}}
```

## lt_to_leq_2

```
lt_to_leq_2 {
\assumes ([]==>[lt(i,j)]) 
\find(==>equals(i,j))
\replacewith([]==>[leq(i,j)]) 

Choices: {}}
```

## make_insert_eq

```
make_insert_eq {
\find(equals(sr,tr)==>)
\addrules [insert_eq {
\find(sr)
\replacewith(tr) 

Choices: {}}] 

Choices: {}}
```

## make_insert_eq_nonrigid

```
make_insert_eq_nonrigid {
\find(equals(s,t)==>)
\addrules [insert_eq_nonrigid {
\find(s)
\sameUpdateLevel\replacewith(t) 

Choices: {}}] 

Choices: {}}
```

## mapEqualityRight

```
mapEqualityRight {
\find(==>equals(m0,m1))
\varcond(\notFreeIn(vy (variable), m1 (Map term)), \notFreeIn(vy (variable), m0 (Map term)))
\replacewith([]==>[all{vy (variable)}(and(equiv(inDomain(m0,vy),inDomain(m1,vy)),imp(inDomain(m0,vy),equals(mapGet(m0,vy),mapGet(m1,vy)))))]) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## mapRemoveUnchanged

```
mapRemoveUnchanged {
\find(equals(m,mapRemove(m,key)))
\replacewith(not(inDomain(m,key))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## mapRemoveUnchanged2

```
mapRemoveUnchanged2 {
\find(equals(mapRemove(m,key),m))
\replacewith(not(inDomain(m,key))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## mapSizeNotNegativeForFiniteMaps

```
mapSizeNotNegativeForFiniteMaps {
\find(mapSize(m))
\add [imp(isFinite(m),geq(mapSize(m),Z(0(#))))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {}}
```

## mapUpdateUnchanged

```
mapUpdateUnchanged {
\find(equals(m,mapUpdate(m,key,value)))
\replacewith(and(inDomain(m,key),equals(mapGet(m,key),value))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## mapUpdateUnchanged2

```
mapUpdateUnchanged2 {
\find(equals(mapUpdate(m,key,value),m))
\replacewith(and(inDomain(m,key),equals(mapGet(m,key),value))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## maxAxiom

```
maxAxiom {
\find(equals(max{x (variable)}(b,t),t2)==>)
\varcond(\notFreeIn(y (variable), t2 (int term)), \notFreeIn(y (variable), t (int term)), \notFreeIn(y (variable), b (boolean term)), \notFreeIn(x (variable), t2 (int term)))
\add []==>[exists{x (variable)}(and(equals(b,TRUE),all{y (variable)}(imp(equals(subst{x (variable)}(y,b),TRUE),leq(subst{x (variable)}(y,t),t)))))] ;
\add [and(all{y (variable)}(subst{x (variable)}(y,imp(equals(b,TRUE),leq(t,t2)))),exists{y (variable)}(subst{x (variable)}(y,and(equals(b,TRUE),equals(t,t2)))))]==>[] 

Choices: {integerSimplificationRules:full}}
```

## measuredByCheck

```
measuredByCheck {
\assumes ([measuredBy(m)]==>[]) 
\find(measuredByCheck(c))
\sameUpdateLevel\replacewith(prec(c,m)) 
\heuristics(simplify)
Choices: {}}
```

## measuredByCheckEmpty

```
measuredByCheckEmpty {
\assumes ([measuredByEmpty]==>[]) 
\find(measuredByCheck(c))
\sameUpdateLevel\replacewith(true) 
\heuristics(simplify)
Choices: {}}
```

## memsetEmpty

```
memsetEmpty {
\find(memset(h,empty,x))
\replacewith(h) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## methodBodyExpand

```
methodBodyExpand {
\find(#allmodal ( (modal operator))\[{ .. #mb ... }\] (post))
\replacewith(#introAtPreDefs(#allmodal ( (modal operator))\[{ .. expand-method-body(#mb) ... }\] (post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## methodCall

```
methodCall {
\find(#allmodal ( (modal operator))\[{ .. #se.#mn(#selist); ... }\] (post))
\sameUpdateLevel\varcond(\not \staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #selist (program SimpleExpression)), )
\add [equals(#se,null)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add []==>[equals(#se,null)] \replacewith(#allmodal ( (modal operator))\[{ .. method-call(#se.#mn(#selist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## methodCall

```
methodCall {
\find(==>#allmodal ( (modal operator))\[{ .. #se.#mn(#selist); ... }\] (post))
\varcond(\not \staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #selist (program SimpleExpression)), )
\add [equals(#se,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[#allmodal ( (modal operator))\[{ .. method-call(#se.#mn(#selist);) ... }\] (post)]) 
\heuristics(method_expand)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## methodCall

```
methodCall {
\find(#allmodal ( (modal operator))\[{ .. #se.#mn(#selist); ... }\] (post))
\varcond(\not \staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #selist (program SimpleExpression)), )
\replacewith(#allmodal ( (modal operator))\[{ .. method-call(#se.#mn(#selist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## methodCall

```
staticMethodCall {
\find(#allmodal ( (modal operator))\[{ .. #se.#mn(#elist); ... }\] (post))
\varcond(\staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #elist (program Expression)), )
\replacewith(#allmodal ( (modal operator))\[{ .. method-call(#se.#mn(#elist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## methodCall

```
staticMethodCallWithAssignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se.#mn(#elist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))), \staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #elist (program Expression)), )
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;method-call(#se.#mn(#elist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## methodCall

```
staticMethodCallStaticViaTypereference {
\find(#allmodal ( (modal operator))\[{ .. #t.#mn(#elist); ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. method-call(#t.#mn(#elist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## methodCall

```
staticMethodCallStaticWithAssignmentViaTypereference {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#t.#mn(#elist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;method-call(#t.#mn(#elist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## methodCall

```
methodCallWithAssignmentWithinClass {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#mn(#elist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;method-call(#mn(#elist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## methodCall

```
methodCallWithinClass {
\find(#allmodal ( (modal operator))\[{ .. #mn(#elist); ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. method-call(#mn(#elist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:disableStaticInitialisation,programRules:Java}}
```

## methodCall

```
staticMethodCall {
\find(#allmodal ( (modal operator))\[{ .. #se.#mn(#elist); ... }\] (post))
\varcond(\staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #elist (program Expression)), )
\replacewith(#allmodal ( (modal operator))\[{ .. static-initialisation(#se.#mn(#elist);)method-call(#se.#mn(#elist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCall

```
staticMethodCallWithAssignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se.#mn(#elist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))), \staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #elist (program Expression)), )
\replacewith(#allmodal ( (modal operator))\[{ .. static-initialisation(#se.#mn(#elist);)#typeof(#lhs) #v0;method-call(#se.#mn(#elist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCall

```
staticMethodCallStaticViaTypereference {
\find(#allmodal ( (modal operator))\[{ .. #t.#mn(#elist); ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. static-initialisation(#t.#mn(#elist);)method-call(#t.#mn(#elist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCall

```
staticMethodCallStaticWithAssignmentViaTypereference {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#t.#mn(#elist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. static-initialisation(#t.#mn(#elist);)#typeof(#lhs) #v0;method-call(#t.#mn(#elist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCall

```
methodCallWithAssignmentWithinClass {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#mn(#elist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. static-initialisation(#mn(#elist);)#typeof(#lhs) #v0;method-call(#mn(#elist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCall

```
methodCallWithinClass {
\find(#allmodal ( (modal operator))\[{ .. #mn(#elist); ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. static-initialisation(#mn(#elist);)method-call(#mn(#elist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCall

```
passiveMethodCallWithinClass {
\find(#allmodal ( (modal operator))\[{ .. @(#mn(#elist);) ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. method-call(#mn(#elist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCall

```
passiveMethodCallStatic {
\find(#allmodal ( (modal operator))\[{ .. @(#t.#mn(#elist);) ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. method-call(#t.#mn(#elist);) ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCall

```
passiveMethodCallWithAssignmentWithinClass {
\find(#allmodal ( (modal operator))\[{ .. #lhs=@(#mn(#elist)); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;method-call(#mn(#elist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCall

```
passiveMethodCallStaticWithAssignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs=@(#t.#mn(#elist)); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;method-call(#t.#mn(#elist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {initialisation:enableStaticInitialisation,programRules:Java}}
```

## methodCallEmpty

```
methodCallEmpty {
\find(#allmodal ( (modal operator))\[{ .. method-frame(#ex): {}
 ... }\] (post))
\replacewith(#allmodal(post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## methodCallEmpty

```
methodCallEmptyNoreturnBox {
\find(\[{ .. method-frame(result->#v0, #ex): {}
 ... }\] (post))
\replacewith(box(post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## methodCallReturn

```
methodCallEmptyReturn {
\find(#allmodal ( (modal operator))\[{ .. method-frame(#ex): {
    return ;
    #slist
  }
 ... }\] (post))
\replacewith(#allmodal(post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## methodCallReturn

```
methodCallReturn {
\find(#allmodal ( (modal operator))\[{ .. method-frame(result->#v0, #ex): {
    return  #se;
    #slist
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. method-frame(#ex): {
    #v0=#se;
  }
 ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## methodCallReturn

```
methodCallReturnIgnoreResult {
\find(#allmodal ( (modal operator))\[{ .. method-frame(#ex): {
    return  #se;
    #slist
  }
 ... }\] (post))
\replacewith(#allmodal(post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## methodCallSuper

```
methodCallSuper {
\find(#allmodal ( (modal operator))\[{ .. super.#mn(#elist); ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. method-call(super.#mn(#elist);) ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## methodCallSuper

```
methodCallWithAssignmentSuper {
\find(#allmodal ( (modal operator))\[{ .. #lhs=super.#mn(#elist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;method-call(super.#mn(#elist);)#lhs=#v0; ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## methodCallThrow

```
methodCallThrow {
\find(#allmodal ( (modal operator))\[{ .. method-frame(#ex): {
    throw  #se;
    #slist
  }
 ... }\] (post))
\varcond(\isLocalVariable (#se (program SimpleExpression)), )
\replacewith(#allmodal ( (modal operator))\[{ .. throw #se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## methodCallThrow

```
methodCallParamThrow {
\find(#allmodal ( (modal operator))\[{ .. method-frame(result->#v0, #ex): {
    throw  #se;
    #slist
  }
 ... }\] (post))
\varcond(\isLocalVariable (#se (program SimpleExpression)), )
\replacewith(#allmodal ( (modal operator))\[{ .. throw #se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## methodCallUnfoldArguments

```
methodCallUnfoldArguments {
\find(#allmodal ( (modal operator))\[{ .. #nsmr ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #evaluate-arguments(#nsmr) ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## methodCallUnfoldArguments

```
methodCallWithAssignmentUnfoldArguments {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nsmr; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #evaluate-arguments(#lhs=#nsmr;) ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## methodCallUnfoldTarget

```
methodCallUnfoldTarget {
\find(#allmodal ( (modal operator))\[{ .. #nse.#mn(#elist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v0;#v0=#nse;#v0.#mn(#elist); ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## methodCallUnfoldTarget

```
methodCallWithAssignmentUnfoldTarget {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse.#mn(#elist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v0;#v0=#nse;#lhs=#v0.#mn(#elist); ... }\] (post)) 
\heuristics(simplify_autoname)
Choices: {programRules:Java}}
```

## methodCallWithAssignment

```
methodCallWithAssignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se.#mn(#selist); ... }\] (post))
\sameUpdateLevel\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))), \not \staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #selist (program SimpleExpression)), )
\add [equals(#se,null)]==>[] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) ;
\add []==>[equals(#se,null)] \replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;method-call(#se.#mn(#selist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## methodCallWithAssignment

```
methodCallWithAssignment {
\find(==>#allmodal ( (modal operator))\[{ .. #lhs=#se.#mn(#selist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))), \not \staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #selist (program SimpleExpression)), )
\add [equals(#se,null)]==>[] \replacewith([]==>[false]) ;
\replacewith([]==>[#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;method-call(#se.#mn(#selist);)#lhs=#v0; ... }\] (post)]) 
\heuristics(method_expand)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## methodCallWithAssignment

```
methodCallWithAssignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#se.#mn(#selist); ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#lhs (program LeftHandSide))), \not \staticMethodReference(#se (program SimpleExpression), #mn (program MethodName), #selist (program SimpleExpression)), )
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs) #v0;method-call(#se.#mn(#selist);)#lhs=#v0; ... }\] (post)) 
\heuristics(method_expand)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## minAxiom

```
minAxiom {
\find(equals(min{x (variable)}(b,t),t2)==>)
\varcond(\notFreeIn(y (variable), t2 (int term)), \notFreeIn(y (variable), t (int term)), \notFreeIn(y (variable), b (boolean term)), \notFreeIn(x (variable), t2 (int term)))
\add []==>[exists{x (variable)}(and(equals(b,TRUE),all{y (variable)}(imp(equals(subst{x (variable)}(y,b),TRUE),geq(subst{x (variable)}(y,t),t)))))] ;
\add [and(all{y (variable)}(subst{x (variable)}(y,imp(equals(b,TRUE),geq(t,t2)))),exists{y (variable)}(subst{x (variable)}(y,and(equals(b,TRUE),equals(t,t2)))))]==>[] 

Choices: {integerSimplificationRules:full}}
```

## minus_distribute

```
minus_distribute_1 {
\find(neg(add(i,i1)))
\replacewith(add(neg(i),neg(i1))) 

Choices: {}}
```

## minus_distribute

```
minus_distribute_2 {
\find(neg(sub(i,i1)))
\replacewith(add(neg(i),i1)) 

Choices: {}}
```

## mod_axiom

```
mod_axiom {
\find(mod(divNum,divDenom))
\replacewith(add(divNum,mul(mul(div(divNum,divDenom),Z(neglit(1(#)))),divDenom))) 
\heuristics(notHumanReadable, defOps_mod)
Choices: {integerSimplificationRules:full}}
```

## mod_homoEq

```
mod_homoEq {
\find(equals(mod(modNumLeft,modDenom),mod(modNumRight,modDenom)))
\replacewith(equals(mod(sub(modNumLeft,modNumRight),modDenom),Z(0(#)))) 
\heuristics(notHumanReadable, defOps_modHomoEq)
Choices: {integerSimplificationRules:full}}
```

## modulo

```
compound_modulo_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse%#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v%#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## modulo

```
compound_modulo_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e%#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0%#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## modulo

```
assignmentModulo {
\find(==>#normalassign ( (modal operator))\[{ .. #loc=#se0%#se1; ... }\] (post))
\replacewith([]==>[not(equals(#se1,Z(0(#))))]) ;
\replacewith([]==>[update-application(elem-update(#loc (program Variable))(javaMod(#se0,#se1)),#normalassign(post))]) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## modulo

```
assignmentModulo {
\find(#normalassign ( (modal operator))\[{ .. #loc=#se0%#se1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaMod(#se0,#se1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## modulo

```
assignmentModulo {
\find(#normalassign ( (modal operator))\[{ .. #loc=#se0%#se1; ... }\] (post))
\replacewith(if-then-else(not(equals(#se1,Z(0(#)))),update-application(elem-update(#loc (program Variable))(javaMod(#se0,#se1)),#normalassign(post)),#normalassign ( (modal operator))\[{ .. throw new java.lang.ArithmeticException (); ... }\] (post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## modulo

```
assignmentModuloBigint1 {
\find(==>#allmodal ( (modal operator))\[{ .. #loc=#seBigint%#seAny; ... }\] (post))
\replacewith([]==>[not(equals(#seAny,Z(0(#))))]) ;
\replacewith([]==>[update-application(elem-update(#loc (program Variable))(mod(#seBigint,#seAny)),#allmodal(post))]) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ban,bigint:on,programRules:Java}}
```

## modulo

```
assignmentModuloBigint2 {
\find(==>#allmodal ( (modal operator))\[{ .. #loc=#seAny%#seBigint; ... }\] (post))
\replacewith([]==>[not(equals(#seBigint,Z(0(#))))]) ;
\replacewith([]==>[update-application(elem-update(#loc (program Variable))(mod(#seAny,#seBigint)),#allmodal(post))]) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ban,bigint:on,programRules:Java}}
```

## modulo

```
assignmentModuloBigint1 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seBigint%#seAny; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(mod(#seBigint,#seAny)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ignore,bigint:on,programRules:Java}}
```

## modulo

```
assignmentModuloBigint2 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seAny%#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(mod(#seAny,#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:ignore,bigint:on,programRules:Java}}
```

## modulo

```
assignmentModuloBigint1 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seBigint%#seAny; ... }\] (post))
\replacewith(if-then-else(not(equals(#seAny,Z(0(#)))),update-application(elem-update(#loc (program Variable))(mod(#seBigint,#seAny)),#allmodal(post)),#allmodal ( (modal operator))\[{ .. throw new java.lang.ArithmeticException (); ... }\] (post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:allow,bigint:on,programRules:Java}}
```

## modulo

```
assignmentModuloBigint2 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seAny%#seBigint; ... }\] (post))
\replacewith(if-then-else(not(equals(#seBigint,Z(0(#)))),update-application(elem-update(#loc (program Variable))(mod(#seAny,#seBigint)),#allmodal(post)),#allmodal ( (modal operator))\[{ .. throw new java.lang.ArithmeticException (); ... }\] (post))) 
\heuristics(executeIntegerAssignment)
Choices: {runtimeExceptions:allow,bigint:on,programRules:Java}}
```

## moduloByteFixpoint

```
moduloByteFixpoint {
\assumes ([inByte(t)]==>[]) 
\find(moduloByte(t))
\sameUpdateLevel\replacewith(t) 

Choices: {intRules:javaSemantics}}
```

## moduloByteFixpoint

```
moduloByteFixpoint {
\assumes ([inByte(t)]==>[]) 
\find(moduloByte(t))
\sameUpdateLevel\replacewith(t) 
\heuristics(simplify)
Choices: {intRules:arithmeticSemanticsCheckingOF}}
```

## moduloByteFixpointInline

```
moduloByteFixpointInline {
\find(moduloByte(t))
\replacewith(if-then-else(inByte(t),t,moduloByte(t))) 
\heuristics(defOps_expandModulo)
Choices: {intRules:javaSemantics}}
```

## moduloByteIsInByte

```
moduloByteIsInByte {
\find(inByte(moduloByte(t)))
\replacewith(true) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## moduloCharFixpoint

```
moduloCharFixpoint {
\assumes ([inChar(t)]==>[]) 
\find(moduloChar(t))
\sameUpdateLevel\replacewith(t) 

Choices: {intRules:javaSemantics}}
```

## moduloCharFixpoint

```
moduloCharFixpoint {
\assumes ([inChar(t)]==>[]) 
\find(moduloChar(t))
\sameUpdateLevel\replacewith(t) 
\heuristics(simplify)
Choices: {intRules:arithmeticSemanticsCheckingOF}}
```

## moduloCharFixpointInline

```
moduloCharFixpointInline {
\find(moduloChar(t))
\replacewith(if-then-else(inChar(t),t,moduloChar(t))) 
\heuristics(defOps_expandModulo)
Choices: {intRules:javaSemantics}}
```

## moduloCharIsInChar

```
moduloCharIsInChar {
\find(inChar(moduloChar(t)))
\replacewith(true) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## moduloIntFixpoint

```
moduloIntFixpoint {
\assumes ([inInt(t)]==>[]) 
\find(moduloInt(t))
\sameUpdateLevel\replacewith(t) 

Choices: {intRules:javaSemantics}}
```

## moduloIntFixpoint

```
moduloIntFixpoint {
\assumes ([inInt(t)]==>[]) 
\find(moduloInt(t))
\sameUpdateLevel\replacewith(t) 
\heuristics(simplify)
Choices: {intRules:arithmeticSemanticsCheckingOF}}
```

## moduloIntFixpointInline

```
moduloIntFixpointInline {
\find(moduloInt(t))
\replacewith(if-then-else(inInt(t),t,moduloInt(t))) 
\heuristics(defOps_expandModulo)
Choices: {intRules:javaSemantics}}
```

## moduloIntIsInInt

```
moduloIntIsInInt {
\find(inInt(moduloInt(t)))
\replacewith(true) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## moduloLongFixpoint

```
moduloLongFixpoint {
\assumes ([inLong(t)]==>[]) 
\find(moduloLong(t))
\sameUpdateLevel\replacewith(t) 

Choices: {intRules:javaSemantics}}
```

## moduloLongFixpoint

```
moduloLongFixpoint {
\assumes ([inLong(t)]==>[]) 
\find(moduloLong(t))
\sameUpdateLevel\replacewith(t) 
\heuristics(simplify)
Choices: {intRules:arithmeticSemanticsCheckingOF}}
```

## moduloLongFixpointInline

```
moduloLongFixpointInline {
\find(moduloLong(t))
\replacewith(if-then-else(inLong(t),t,moduloLong(t))) 
\heuristics(defOps_expandModulo)
Choices: {intRules:javaSemantics}}
```

## moduloLongIsInLong

```
moduloLongIsInLong {
\find(inLong(moduloLong(t)))
\replacewith(true) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## moduloShortFixpoint

```
moduloShortFixpoint {
\assumes ([inShort(t)]==>[]) 
\find(moduloShort(t))
\sameUpdateLevel\replacewith(t) 

Choices: {intRules:javaSemantics}}
```

## moduloShortFixpoint

```
moduloShortFixpoint {
\assumes ([inShort(t)]==>[]) 
\find(moduloShort(t))
\sameUpdateLevel\replacewith(t) 
\heuristics(simplify)
Choices: {intRules:arithmeticSemanticsCheckingOF}}
```

## moduloShortFixpointInline

```
moduloShortFixpointInline {
\find(moduloShort(t))
\replacewith(if-then-else(inShort(t),t,moduloShort(t))) 
\heuristics(defOps_expandModulo)
Choices: {intRules:javaSemantics}}
```

## moduloShortIsInShort

```
moduloShortIsInShort {
\find(inShort(moduloShort(t)))
\replacewith(true) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## mul_assoc

```
mul_assoc {
\find(mul(mul(i,i0),i1))
\replacewith(mul(i,mul(i0,i1))) 

Choices: {}}
```

## mul_comm

```
mul_comm {
\find(mul(i0,i1))
\replacewith(mul(i1,i0)) 

Choices: {}}
```

## mul_literals

```
mul_literals {
\find(mul(Z(iz),Z(jz)))
\replacewith(#mul(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## mult_eq_1_iff

```
mult_eq_1_iff {
\find(equals(mul(i0,i1),Z(1(#))))
\replacewith(or(and(equals(i0,Z(1(#))),equals(i1,Z(1(#)))),and(equals(i0,Z(neglit(1(#)))),equals(i1,Z(neglit(1(#))))))) 

Choices: {}}
```

## mult_eq_self_iff

```
mult_eq_self_iff {
\find(equals(i0,mul(i0,i1)))
\replacewith(or(equals(i0,Z(0(#))),equals(i1,Z(1(#))))) 

Choices: {}}
```

## mult_leq_0_iff

```
mult_leq_0_iff {
\find(leq(mul(i0,i1),Z(0(#))))
\replacewith(or(and(leq(i0,Z(0(#))),leq(Z(0(#)),i1)),and(leq(Z(0(#)),i0),leq(i1,Z(0(#)))))) 

Choices: {}}
```

## mult_less_0_iff

```
mult_less_0_iff {
\find(lt(mul(i0,i1),Z(0(#))))
\replacewith(or(and(lt(i0,Z(0(#))),lt(Z(0(#)),i1)),and(lt(Z(0(#)),i0),lt(i1,Z(0(#)))))) 

Choices: {}}
```

## mult_neg

```
mult_neg {
\find(and(lt(i0,Z(0(#))),lt(i1,Z(0(#)))))
\replacewith(lt(Z(0(#)),mul(i0,i1))) 

Choices: {}}
```

## mult_pos

```
mult_pos {
\find(and(lt(Z(0(#)),i0),lt(Z(0(#)),i1)))
\replacewith(lt(Z(0(#)),mul(i0,i1))) 

Choices: {}}
```

## mult_pos_neg

```
mult_pos_neg {
\find(and(lt(i0,Z(0(#))),lt(Z(0(#)),i1)))
\replacewith(lt(mul(i0,i1),Z(0(#)))) 

Choices: {}}
```

## multiplication

```
compound_multiplication_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse*#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v*#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## multiplication

```
compound_multiplication_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e*#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0*#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## multiplication

```
assignmentMultiplicationInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0*#seCharByteShortInt1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaMulInt(#seCharByteShortInt0,#seCharByteShortInt1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## multiplication

```
assignmentMultiplicationLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt*#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaMulLong(#seCharByteShortInt,#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## multiplication

```
assignmentMultiplicationLong2 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong*#seCharByteShortInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaMulLong(#seLong,#seCharByteShortInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## multiplication

```
assignmentMultiplicationLong3 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong0*#seLong1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaMulLong(#seLong0,#seLong1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## multiplication

```
assignmentMultiplicationBigint1 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seBigint*#seAny; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(mul(#seBigint,#seAny)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## multiplication

```
assignmentMultiplicationBigint2 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seAny*#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(mul(#seAny,#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## multiply_2_inEq0

```
multiply_2_inEq0 {
\assumes ([leq(multFacLeft,multFacRight)]==>[]) 
\find(leq(multLeft,multRight)==>)
\add [geq(mul(multLeft,multFacLeft),add(add(mul(neg(multRight),multFacRight),mul(multRight,multFacLeft)),mul(multLeft,multFacRight)))]==>[] 
\heuristics(inEqSimp_nonLin_multiply, inEqSimp_nonLin)
Choices: {integerSimplificationRules:full}}
```

## multiply_2_inEq1

```
multiply_2_inEq1 {
\assumes ([geq(multFacLeft,multFacRight)]==>[]) 
\find(leq(multLeft,multRight)==>)
\add [leq(mul(multLeft,multFacLeft),add(add(mul(neg(multRight),multFacRight),mul(multRight,multFacLeft)),mul(multLeft,multFacRight)))]==>[] 
\heuristics(inEqSimp_nonLin_multiply, inEqSimp_nonLin)
Choices: {integerSimplificationRules:full}}
```

## multiply_2_inEq2

```
multiply_2_inEq2 {
\assumes ([leq(multFacLeft,multFacRight)]==>[]) 
\find(geq(multLeft,multRight)==>)
\add [leq(mul(multLeft,multFacLeft),add(add(mul(neg(multRight),multFacRight),mul(multRight,multFacLeft)),mul(multLeft,multFacRight)))]==>[] 
\heuristics(inEqSimp_nonLin_multiply, inEqSimp_nonLin)
Choices: {integerSimplificationRules:full}}
```

## multiply_2_inEq3

```
multiply_2_inEq3 {
\assumes ([geq(multFacLeft,multFacRight)]==>[]) 
\find(geq(multLeft,multRight)==>)
\add [geq(mul(multLeft,multFacLeft),add(add(mul(neg(multRight),multFacRight),mul(multRight,multFacLeft)),mul(multLeft,multFacRight)))]==>[] 
\heuristics(inEqSimp_nonLin_multiply, inEqSimp_nonLin)
Choices: {integerSimplificationRules:full}}
```

## multiply_distribute

```
multiply_distribute_1 {
\find(mul(add(i0,i1),add(j0,j1)))
\replacewith(add(add(mul(i0,j0),mul(i0,j1)),add(mul(i1,j0),mul(i1,j1)))) 

Choices: {}}
```

## multiply_distribute

```
multiply_distribute_2 {
\find(mul(add(i0,i1),sub(j0,j1)))
\replacewith(add(sub(mul(i0,j0),mul(i0,j1)),sub(mul(i1,j0),mul(i1,j1)))) 

Choices: {}}
```

## multiply_distribute

```
multiply_distribute_3 {
\find(mul(sub(i0,i1),sub(j0,j1)))
\replacewith(add(sub(mul(i0,j0),mul(i0,j1)),sub(mul(i1,j1),mul(i1,j0)))) 

Choices: {}}
```

## multiply_distribute

```
mul_distribute_4 {
\find(mul(i0,add(i1,i2)))
\replacewith(add(mul(i0,i1),mul(i0,i2))) 

Choices: {}}
```

## multiply_distribute

```
mul_distribute_5 {
\find(mul(add(i1,i2),i0))
\replacewith(add(mul(i0,i1),mul(i0,i2))) 

Choices: {}}
```

## multiply_eq

```
multiply_eq {
\find(equals(multLeft,multRight)==>)
\add [equals(mul(multLeft,multFac),mul(multRight,multFac))]==>[] 

Choices: {integerSimplificationRules:full}}
```

## multiply_inEq0

```
multiply_inEq0 {
\find(leq(multLeft,multRight)==>)
\add [if-then-else(geq(multFac,Z(0(#))),leq(mul(multLeft,multFac),mul(multRight,multFac)),geq(mul(multLeft,multFac),mul(multRight,multFac)))]==>[] 

Choices: {integerSimplificationRules:full}}
```

## multiply_inEq1

```
multiply_inEq1 {
\find(geq(multLeft,multRight)==>)
\add [if-then-else(geq(multFac,Z(0(#))),geq(mul(multLeft,multFac),mul(multRight,multFac)),leq(mul(multLeft,multFac),mul(multRight,multFac)))]==>[] 

Choices: {integerSimplificationRules:full}}
```

## narrow type

```
castAdd {
\assumes ([equals(CSub::instance(strictCTerm2),TRUE)]==>[]) 
\find(strictCTerm2)
\sameUpdateLevel\replacewith(CSub::cast(strictCTerm2)) 

Choices: {}}
```

## narrowSelectArrayType

```
narrowSelectArrayType {
\assumes ([wellFormed(h)]==>[equals(o,null)]) 
\find(beta::select(h,o,arr(idx)))
\sameUpdateLevel\varcond(\hasSort(\elemSort(o (java.lang.Object term)), alpha), \strict\sub(alpha, beta), )
\replacewith(alpha::select(h,o,arr(idx))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## narrowSelectType

```
narrowSelectType {
\assumes ([wellFormed(h)]==>[]) 
\find(beta::select(h,o,f))
\varcond(\fieldType(f (Field term), alpha), \strict\sub(alpha, beta), )
\replacewith(alpha::select(h,o,f)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## neg_literal

```
neg_literal {
\find(neg(Z(iz)))
\replacewith(Z(neglit(iz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## neq_and

```
neq_and {
\find(and(phi,not(phi)))
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## neq_and_2

```
neq_and_2 {
\find(and(not(phi),phi))
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## neq_and_3

```
neq_and_3 {
\find(and(and(psi,phi),not(phi)))
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## neq_and_4

```
neq_and_4 {
\find(and(and(psi,not(phi)),phi))
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## neq_or

```
neq_or {
\find(or(phi,not(phi)))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## neq_or_2

```
neq_or_2 {
\find(or(not(phi),phi))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## neq_or_3

```
neq_or_3 {
\find(or(or(psi,phi),not(phi)))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## neq_or_4

```
neq_or_4 {
\find(or(or(psi,not(phi)),phi))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## newSym_eq

```
newSym_eq {
\find(equals(mul(newSymLeft,newSymLeftCoeff),newSymRight)==>)
\add [equals(newSymLeft,add(l,newSymDef))]==>[] 
\heuristics(polySimp_newSmallSym, polySimp_newSym, polySimp_leftNonUnit)
Choices: {integerSimplificationRules:full}}
```

## nnf_ex2all

```
nnf_ex2all {
\find(==>exists{u (variable)}(phi))
\replacewith([all{u (variable)}(not(phi))]==>[]) 
\heuristics(notHumanReadable, moveQuantToLeft)
Choices: {}}
```

## nnf_imp2or

```
nnf_imp2or {
\find(imp(phi,psi))
\replacewith(or(not(phi),psi)) 
\heuristics(notHumanReadable, negationNormalForm)
Choices: {}}
```

## nnf_notAll

```
nnf_notAll {
\find(not(all{u (variable)}(phi)))
\replacewith(exists{u (variable)}(not(phi))) 
\heuristics(notHumanReadable, negationNormalForm)
Choices: {}}
```

## nnf_notAnd

```
nnf_notAnd {
\find(not(and(phi,psi)))
\replacewith(or(not(phi),not(psi))) 
\heuristics(notHumanReadable, negationNormalForm)
Choices: {}}
```

## nnf_notEqv

```
nnf_notEqv {
\find(not(equiv(phi,psi)))
\replacewith(equiv(phi,not(psi))) 
\heuristics(notHumanReadable, negationNormalForm)
Choices: {}}
```

## nnf_notEx

```
nnf_notEx {
\find(not(exists{u (variable)}(phi)))
\replacewith(all{u (variable)}(not(phi))) 
\heuristics(notHumanReadable, negationNormalForm)
Choices: {}}
```

## nnf_notOr

```
nnf_notOr {
\find(not(or(phi,psi)))
\replacewith(and(not(phi),not(psi))) 
\heuristics(notHumanReadable, negationNormalForm)
Choices: {}}
```

## noElementOfSupersetImpliesNoElementOfSubset

```
noElementOfSupersetImpliesNoElementOfSubset {
\assumes ([subset(s,s2)]==>[]) 
\find(==>elementOf(o,f,s2))
\add []==>[elementOf(o,f,s)] 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## nonEmptyPermission

```
nonEmptyPermission {
\find(nonEmptyPermission(p))
\varcond(\notFreeIn(pp (variable), p (Permission term)), \notFreeIn(ol (variable), p (Permission term)))
\replacewith(exists{ol (variable)}(exists{pp (variable)}(equals(p,slice(ol,pp))))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## nonNull

```
nonNull {
\find(nonNull(heapSV,o,depth))
\varcond(\notFreeIn(i (variable), depth (int term)), \notFreeIn(i (variable), heapSV (Heap term)), \notFreeIn(i (variable), o (java.lang.Object term)), \isReferenceArray(o (java.lang.Object term)), )
\replacewith(and(not(equals(o,null)),imp(gt(depth,Z(0(#))),all{i (variable)}(imp(and(leq(Z(0(#)),i),lt(i,length(o))),nonNull(heapSV,java.lang.Object::select(heapSV,o,arr(i)),sub(depth,Z(1(#))))))))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## nonNullZero

```
nonNullZero {
\find(nonNull(heapSV,o,Z(0(#))))
\replacewith(not(equals(o,null))) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## notInDomain

```
notInDomain {
\find(==>inDomain(m,x))
\add [equals(mapGet(m,x),mapUndef)]==>[] 
\heuristics(inReachableStateImplication)
Choices: {}}
```

## notLeft

```
notLeft {
\find(not(b)==>)
\replacewith([]==>[b]) 
\heuristics(alpha)
Choices: {}}
```

## notRight

```
notRight {
\find(==>not(b))
\replacewith([b]==>[]) 
\heuristics(alpha)
Choices: {}}
```

## nullCreated

```
nullCreated {
\add [or(all{h (variable)}(equals(boolean::select(h,null,java.lang.Object::<created>),TRUE)),all{h (variable)}(equals(boolean::select(h,null,java.lang.Object::<created>),FALSE)))]==>[] 

Choices: {programRules:Java}}
```

## nullString

```
nullString {
\find(strContent(null))
\replacewith(seqConcat(seqSingleton(C(0(1(1(#))))),seqConcat(seqSingleton(C(7(1(1(#))))),seqConcat(seqSingleton(C(8(0(1(#))))),seqSingleton(C(8(0(1(#))))))))) 
\heuristics(concrete)
Choices: {}}
```

## null_can_always_be_stored_in_a_reference_type_array

```
null_can_always_be_stored_in_a_reference_type_array {
\assumes ([]==>[equals(array,null)]) 
\find(arrayStoreValid(array,null))
\sameUpdateLevel\varcond(\isReferenceArray(array (GOS term)), )
\replacewith(true) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## onlyCreatedObjectsAreInLocSets

```
onlyCreatedObjectsAreInLocSets {
\assumes ([wellFormed(h)]==>[]) 
\find(elementOf(o2,f2,LocSet::select(h,o,f))==>)
\add [or(equals(o2,null),equals(boolean::select(h,o2,java.lang.Object::<created>),TRUE))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {programRules:Java}}
```

## onlyCreatedObjectsAreInLocSetsEQ

```
onlyCreatedObjectsAreInLocSetsEQ {
\assumes ([wellFormed(h),equals(LocSet::select(h,o,f),EQ)]==>[]) 
\find(elementOf(o2,f2,EQ)==>)
\add [or(equals(o2,null),equals(boolean::select(h,o2,java.lang.Object::<created>),TRUE))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {programRules:Java}}
```

## onlyCreatedObjectsAreObserved

```
onlyCreatedObjectsAreObserved {
\find(obs)
\sameUpdateLevel\varcond(\isObserver (obs (deltaObject term), h (Heap term)), )
\add [or(equals(obs,null),equals(boolean::select(h,obs,java.lang.Object::<created>),TRUE))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {programRules:Java}}
```

## onlyCreatedObjectsAreObservedInLocSets

```
onlyCreatedObjectsAreObservedInLocSets {
\find(elementOf(o,f,obs)==>)
\varcond(\isObserver (obs (LocSet term), h (Heap term)), )
\add [or(equals(o,null),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {programRules:Java}}
```

## onlyCreatedObjectsAreObservedInLocSetsEQ

```
onlyCreatedObjectsAreObservedInLocSetsEQ {
\assumes ([equals(obs,EQ)]==>[]) 
\find(elementOf(o,f,EQ)==>)
\varcond(\isObserver (obs (LocSet term), h (Heap term)), )
\add [or(equals(o,null),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {programRules:Java}}
```

## onlyCreatedObjectsAreReferenced

```
onlyCreatedObjectsAreReferenced {
\assumes ([wellFormed(h)]==>[]) 
\find(deltaObject::select(h,o,f))
\sameUpdateLevel\add [or(equals(deltaObject::select(h,o,f),null),equals(boolean::select(h,deltaObject::select(h,o,f),java.lang.Object::<created>),TRUE))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {programRules:Java}}
```

## only_created_objects_are_reachable

```
only_created_objects_are_reachable {
\assumes ([wellFormed(h)]==>[equals(o,null)]) 
\find(reach(h,s,o,o2,n)==>)
\add [or(not(equals(boolean::select(h,o,java.lang.Object::<created>),TRUE)),equals(boolean::select(h,o2,java.lang.Object::<created>),TRUE))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {reach:on}}
```

## optAxiom

```
optAxiom {
\find(match(opt(rexp),string))
\replacewith(or(match(repeat(rexp,Z(0(#))),string),match(rexp,string))) 
\heuristics(simplify)
Choices: {Strings:on}}
```

## optEmpty

```
optEmpty {
\find(match(opt(rexp),seqEmpty))
\replacewith(true) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## orJIntDef

```
orJIntDef {
\find(orJint(left,right))
\replacewith(moduloInt(binaryOr(left,right))) 
\heuristics(simplify)
Choices: {}}
```

## orJintInInt

```
orJintInInt {
\find(orJint(left,right))
\sameUpdateLevel\add [inInt(orJint(left,right))]==>[] 
\heuristics(userTaclets1)
Choices: {}}
```

## orLeft

```
orLeft {
\find(or(b,c)==>)
\replacewith([c]==>[]) ;
\replacewith([b]==>[]) 
\heuristics(beta)
Choices: {}}
```

## orRight

```
orRight {
\find(==>or(b,c))
\replacewith([]==>[b,c]) 
\heuristics(alpha)
Choices: {}}
```

## parallelWithSkip1

```
parallelWithSkip1 {
\find(parallel-upd(skip,u))
\replacewith(u) 
\heuristics(update_elim)
Choices: {}}
```

## parallelWithSkip2

```
parallelWithSkip2 {
\find(parallel-upd(u,skip))
\replacewith(u) 
\heuristics(update_elim)
Choices: {}}
```

## partition_inequation

```
partition_inequation {
\assumes ([]==>[lt(i,i0)]) 
\find(lt(i,i1)==>)
\add []==>[lt(i1,i0)] 

Choices: {}}
```

## permOwner1

```
permOwner1 {
\find(owner1(o1))
\replacewith(consPermissionOwnerList(o1,emptyPermissionOwnerList)) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## permOwner2

```
permOwner2 {
\find(owner2(o1,o2))
\replacewith(consPermissionOwnerList(o1,consPermissionOwnerList(o2,emptyPermissionOwnerList))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## permOwner3

```
permOwner3 {
\find(owner3(o1,o2,o3))
\replacewith(consPermissionOwnerList(o1,consPermissionOwnerList(o2,consPermissionOwnerList(o3,emptyPermissionOwnerList)))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## permOwner4

```
permOwner4 {
\find(owner4(o1,o2,o3,o4))
\replacewith(consPermissionOwnerList(o1,consPermissionOwnerList(o2,consPermissionOwnerList(o3,consPermissionOwnerList(o4,emptyPermissionOwnerList))))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## permSlice1

```
permSlice1 {
\find(slice1(pol1))
\replacewith(slice(pol1,emptyPermission)) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## permSlice2

```
permSlice2 {
\find(slice2(pol1,pol2))
\replacewith(slice(pol1,slice(pol2,emptyPermission))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## permissionDefaultValue

```
permissionDefaultValue {
\find(Permission::defaultValue)
\replacewith(initFullPermission) 
\heuristics(simplify)
Choices: {}}
```

## permissionTransferReturnIdentity

```
permissionTransferReturnIdentity {
\find(returnPermission(o2,o1,transferPermission(FALSE,o1,o2,Z(0(#)),p)))
\replacewith(p) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## permissionTransferReturnIdentityEQ

```
permissionTransferReturnIdentityEQ {
\assumes ([equals(p2,transferPermission(FALSE,o1,o2,Z(0(#)),p1))]==>[]) 
\find(returnPermission(o2,o1,p2))
\replacewith(p1) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## polyDiv_pullOut

```
polyDiv_pullOut {
\find(div(divNum,divDenom))
\replacewith(if-then-else(equals(divDenom,Z(0(#))),div(divNum,Z(0(#))),add(div(add(divNum,mul(mul(polyDivCoeff,Z(neglit(1(#)))),divDenom)),divDenom),polyDivCoeff))) 
\heuristics(notHumanReadable, defOps_divModPullOut, polyDivision)
Choices: {integerSimplificationRules:full}}
```

## polyDiv_zero

```
polyDiv_zero {
\find(div(Z(0(#)),divDenom))
\replacewith(if-then-else(equals(divDenom,Z(0(#))),div(Z(0(#)),Z(0(#))),Z(0(#)))) 
\heuristics(polyDivision)
Choices: {integerSimplificationRules:full}}
```

## polyMod_pullOut

```
polyMod_pullOut {
\find(mod(divNum,divDenom))
\replacewith(mod(add(divNum,mul(mul(polyDivCoeff,Z(neglit(1(#)))),divDenom)),divDenom)) 
\heuristics(notHumanReadable, defOps_divModPullOut, polyDivision)
Choices: {integerSimplificationRules:full}}
```

## polyMod_zero

```
polyMod_zero {
\find(mod(Z(0(#)),divDenom))
\replacewith(Z(0(#))) 
\heuristics(concrete, polyDivision)
Choices: {integerSimplificationRules:full}}
```

## polySimp_addAssoc

```
polySimp_addAssoc {
\find(add(addAssocPoly0,add(addAssocPoly1,addAssocMono)))
\replacewith(add(add(addAssocPoly0,addAssocPoly1),addAssocMono)) 
\heuristics(polySimp_addAssoc, polySimp_expand)
Choices: {}}
```

## polySimp_addComm0

```
polySimp_addComm0 {
\find(add(commLeft,commRight))
\replacewith(add(commRight,commLeft)) 
\heuristics(polySimp_addOrder, polySimp_expand)
Choices: {}}
```

## polySimp_addComm1

```
polySimp_addComm1 {
\find(add(add(i0,commLeft),commRight))
\replacewith(add(add(i0,commRight),commLeft)) 
\heuristics(polySimp_addOrder, polySimp_expand)
Choices: {}}
```

## polySimp_addLiterals

```
polySimp_addLiterals {
\find(add(add(i,Z(iz)),Z(jz)))
\replacewith(add(i,#add(Z(iz),Z(jz)))) 
\heuristics(simplify_literals)
Choices: {}}
```

## polySimp_critPair

```
polySimp_critPair {
\assumes ([equals(cpLeft1,cpRight1)]==>[]) 
\find(equals(cpLeft2,cpRight2)==>)
\add [equals(add(mul(#divideLCRMonomials(cpLeft2,cpLeft1),add(mul(Z(neglit(1(#))),cpRight1),cpLeft1)),mul(#divideLCRMonomials(cpLeft1,cpLeft2),add(cpRight2,mul(Z(neglit(1(#))),cpLeft2)))),Z(0(#)))]==>[] 
\heuristics(notHumanReadable, polySimp_critPair, polySimp_saturate)
Choices: {}}
```

## polySimp_elimNeg

```
polySimp_elimNeg {
\find(neg(i))
\replacewith(mul(i,Z(neglit(1(#))))) 
\heuristics(polySimp_elimSubNeg, polySimp_expand)
Choices: {}}
```

## polySimp_elimOne

```
polySimp_elimOne {
\find(mul(i,Z(1(#))))
\replacewith(i) 
\heuristics(polySimp_elimOneRight, polySimp_expand)
Choices: {}}
```

## polySimp_elimOneLeft0

```
polySimp_elimOneLeft0 {
\find(mul(Z(1(#)),i))
\replacewith(i) 
\heuristics(polySimp_elimOneLeft, polySimp_expand)
Choices: {}}
```

## polySimp_elimOneLeft1

```
polySimp_elimOneLeft1 {
\find(mul(mul(i0,Z(1(#))),i))
\replacewith(mul(i0,i)) 
\heuristics(polySimp_elimOneLeft, polySimp_expand)
Choices: {}}
```

## polySimp_elimSub

```
polySimp_elimSub {
\find(sub(i,i0))
\replacewith(add(i,mul(i0,Z(neglit(1(#)))))) 
\heuristics(polySimp_elimSubNeg, polySimp_expand)
Choices: {}}
```

## polySimp_homoEq

```
polySimp_homoEq {
\find(equals(homoLeft,homoRight))
\replacewith(equals(add(homoRight,mul(homoLeft,Z(neglit(1(#))))),Z(0(#)))) 
\heuristics(notHumanReadable, polySimp_homo, polySimp_expand)
Choices: {}}
```

## polySimp_invertEq

```
polySimp_invertEq {
\find(equals(invertLeft,invertRight))
\replacewith(equals(mul(invertLeft,Z(neglit(1(#)))),mul(invertRight,Z(neglit(1(#)))))) 
\heuristics(polySimp_normalise, polySimp_directEquations)
Choices: {}}
```

## polySimp_mulAssoc

```
polySimp_mulAssoc {
\find(mul(mulAssocMono0,mul(mulAssocMono1,mulAssocAtom)))
\replacewith(mul(mul(mulAssocMono0,mulAssocMono1),mulAssocAtom)) 
\heuristics(polySimp_mulAssoc, polySimp_expand)
Choices: {}}
```

## polySimp_mulComm0

```
polySimp_mulComm0 {
\find(mul(commLeft,commRight))
\replacewith(mul(commRight,commLeft)) 
\heuristics(polySimp_mulOrder, polySimp_expand)
Choices: {}}
```

## polySimp_mulComm1

```
polySimp_mulComm1 {
\find(mul(mul(i0,commLeft),commRight))
\replacewith(mul(mul(i0,commRight),commLeft)) 
\heuristics(polySimp_mulOrder, polySimp_expand)
Choices: {}}
```

## polySimp_mulLiterals

```
polySimp_mulLiterals {
\find(mul(mul(i,Z(iz)),Z(jz)))
\replacewith(mul(i,#mul(Z(iz),Z(jz)))) 
\heuristics(simplify_literals)
Choices: {}}
```

## polySimp_pullOutFactor0

```
polySimp_pullOutFactor0 {
\find(add(mul(pullOutCommon,pullOutLeft),mul(pullOutCommon,pullOutRight)))
\replacewith(mul(pullOutCommon,add(pullOutLeft,pullOutRight))) 
\heuristics(polySimp_pullOutFactor, polySimp_expand)
Choices: {}}
```

## polySimp_pullOutFactor0b

```
polySimp_pullOutFactor0b {
\find(add(add(i0,mul(pullOutCommon,pullOutLeft)),mul(pullOutCommon,pullOutRight)))
\replacewith(add(i0,mul(pullOutCommon,add(pullOutLeft,pullOutRight)))) 
\heuristics(polySimp_pullOutFactor, polySimp_expand)
Choices: {}}
```

## polySimp_pullOutFactor1

```
polySimp_pullOutFactor1 {
\find(add(pullOutCommon,mul(pullOutCommon,pullOutRight)))
\replacewith(mul(pullOutCommon,add(Z(1(#)),pullOutRight))) 
\heuristics(polySimp_pullOutFactor, polySimp_expand)
Choices: {}}
```

## polySimp_pullOutFactor1b

```
polySimp_pullOutFactor1b {
\find(add(add(i0,pullOutCommon),mul(pullOutCommon,pullOutRight)))
\replacewith(add(i0,mul(pullOutCommon,add(Z(1(#)),pullOutRight)))) 
\heuristics(polySimp_pullOutFactor, polySimp_expand)
Choices: {}}
```

## polySimp_pullOutFactor2

```
polySimp_pullOutFactor2 {
\find(add(mul(pullOutCommon,pullOutLeft),pullOutCommon))
\replacewith(mul(pullOutCommon,add(pullOutLeft,Z(1(#))))) 
\heuristics(polySimp_pullOutFactor, polySimp_expand)
Choices: {}}
```

## polySimp_pullOutFactor2b

```
polySimp_pullOutFactor2b {
\find(add(add(i0,mul(pullOutCommon,pullOutLeft)),pullOutCommon))
\replacewith(add(i0,mul(pullOutCommon,add(pullOutLeft,Z(1(#)))))) 
\heuristics(polySimp_pullOutFactor, polySimp_expand)
Choices: {}}
```

## polySimp_pullOutFactor3

```
polySimp_pullOutFactor3 {
\find(add(pullOutCommon,pullOutCommon))
\replacewith(mul(pullOutCommon,Z(2(#)))) 
\heuristics(polySimp_pullOutFactor, polySimp_expand)
Choices: {}}
```

## polySimp_pullOutFactor3b

```
polySimp_pullOutFactor3b {
\find(add(add(i0,pullOutCommon),pullOutCommon))
\replacewith(add(i0,mul(pullOutCommon,Z(2(#))))) 
\heuristics(polySimp_pullOutFactor, polySimp_expand)
Choices: {}}
```

## polySimp_rightDist

```
polySimp_rightDist {
\find(mul(distCoeff,add(distSummand0,distSummand1)))
\replacewith(add(mul(distCoeff,distSummand0),mul(distSummand1,distCoeff))) 
\heuristics(polySimp_dist, polySimp_expand)
Choices: {}}
```

## polySimp_sepNegMonomial

```
polySimp_sepNegMonomial {
\find(equals(add(sepResidue,sepNegMono),Z(0(#))))
\replacewith(equals(mul(sepNegMono,Z(neglit(1(#)))),sepResidue)) 
\heuristics(polySimp_balance, polySimp_directEquations)
Choices: {}}
```

## polySimp_sepPosMonomial

```
polySimp_sepPosMonomial {
\find(equals(add(sepResidue,sepPosMono),Z(0(#))))
\replacewith(equals(sepPosMono,mul(sepResidue,Z(neglit(1(#)))))) 
\heuristics(notHumanReadable, polySimp_balance, polySimp_directEquations)
Choices: {}}
```

## poolIsInjective

```
poolIsInjective {
\find(equals(strPool(slit1),strPool(slit2)))
\replacewith(equals(slit1,slit2)) 
\heuristics(simplify)
Choices: {}}
```

## poolKeyIsContentOfValue

```
poolKeyIsContentOfValue {
\find(strContent(strPool(slit)))
\replacewith(slit) 
\heuristics(simplify)
Choices: {}}
```

## pos_mult_eq_1_iff

```
pos_mult_eq_1_iff {
\find(imp(lt(Z(0(#)),i0),equals(mul(i0,i1),Z(1(#)))))
\replacewith(and(equals(i0,Z(1(#))),equals(i1,Z(1(#))))) 

Choices: {}}
```

## postdecrement

```
postdecrement {
\find(#allmodal ( (modal operator))\[{ .. #lhs1--; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))#lhs1-1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postdecrement_array

```
postdecrement_array {
\find(#allmodal ( (modal operator))\[{ .. #e[#e0]--; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#e0) #v0 = #e0;#v[#v0]=(#typeof(#e[#e0]))(#v[#v0]-1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postdecrement_assignment

```
postdecrement_assignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1--; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#lhs0 (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs0) #v = #lhs1;#lhs1=(#typeof(#lhs1))(#lhs1-1);#lhs0=#v; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postdecrement_assignment_array

```
postdecrement_assignment_array {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e[#e0]--; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#lhs0 (program LeftHandSide))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#e0) #v0 = #e0;#typeof(#lhs0) #v1 = #v[#v0];#v[#v0]=(#typeof(#e[#e0]))(#v[#v0]-1);#lhs0=#v1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postdecrement_assignment_attribute

```
postdecrement_assignment_attribute {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e.#attribute--; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#lhs0 (program LeftHandSide))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#lhs0) #v1 = #v.#attribute;#v.#attribute=(#typeof(#attribute))(#v.#attribute-1);#lhs0=#v1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postdecrement_attribute

```
postdecrement_attribute {
\find(#allmodal ( (modal operator))\[{ .. #e.#attribute--; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#v.#attribute=(#typeof(#attribute))(#v.#attribute-1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postincrement

```
postincrement {
\find(#allmodal ( (modal operator))\[{ .. #lhs1++; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1+1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postincrement_array

```
postincrement_array {
\find(#allmodal ( (modal operator))\[{ .. #e[#e0]++; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#e0) #v0 = #e0;#v[#v0]=(#typeof(#e[#e0]))(#v[#v0]+1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postincrement_assignment

```
postincrement_assignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#lhs1++; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#lhs0 (program LeftHandSide))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#lhs0) #v = #lhs1;#lhs1=(#typeof(#lhs1))(#lhs1+1);#lhs0=#v; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postincrement_assignment_array

```
postincrement_assignment_array {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e[#e0]++; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#lhs0 (program LeftHandSide))), \new(#v0 (program Variable), \typeof(#e0 (program Expression))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#e0) #v0 = #e0;#typeof(#lhs0) #v1 = #v[#v0];#v[#v0]=(#typeof(#e[#e0]))(#v[#v0]+1);#lhs0=#v1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postincrement_assignment_attribute

```
postincrement_assignment_attribute {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=#e.#attribute++; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#lhs0 (program LeftHandSide))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#lhs0) #v1 = #v.#attribute;#v.#attribute=(#typeof(#attribute))(#v.#attribute+1);#lhs0=#v1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## postincrement_attribute

```
postincrement_attribute {
\find(#allmodal ( (modal operator))\[{ .. #e.#attribute++; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#v.#attribute=(#typeof(#attribute))(#v.#attribute+1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## powDef

```
powDef {
\find(pow(base,exp))
\varcond(\notFreeIn(x (variable), base (int term)), \notFreeIn(x (variable), exp (int term)))
\replacewith(if-then-else(geq(exp,Z(0(#))),bprod{x (variable)}(Z(0(#)),exp,base),undefinedPow(base,exp))) 

Choices: {integerSimplificationRules:full}}
```

## pow_literals

```
pow_literals {
\find(pow(Z(iz),Z(jz)))
\replacewith(#pow(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## precOfInt

```
precOfInt {
\find(prec(a,b))
\replacewith(and(leq(Z(0(#)),a),lt(a,b))) 
\heuristics(simplify)
Choices: {}}
```

## precOfIntPair

```
precOfIntPair {
\find(prec(a,pair(b,x)))
\replacewith(and(leq(Z(0(#)),a),leq(a,b))) 
\heuristics(simplify)
Choices: {}}
```

## precOfPair

```
precOfPair {
\find(prec(pair(a1,b1),pair(a2,b2)))
\replacewith(or(prec(a1,a2),and(equals(a1,a2),prec(b1,b2)))) 
\heuristics(simplify)
Choices: {}}
```

## precOfPairInt

```
precOfPairInt {
\find(prec(pair(a,x),b))
\replacewith(and(leq(Z(0(#)),a),lt(a,b))) 
\heuristics(simplify)
Choices: {}}
```

## precOfSeq

```
precOfSeq {
\find(prec(s1,s2))
\varcond(\notFreeIn(jv (variable), s2 (Seq term)), \notFreeIn(jv (variable), s1 (Seq term)), \notFreeIn(iv (variable), s2 (Seq term)), \notFreeIn(iv (variable), s1 (Seq term)))
\replacewith(or(exists{iv (variable)}(and(and(and(and(leq(Z(0(#)),iv),lt(iv,seqLen(s1))),lt(iv,seqLen(s2))),prec(any::seqGet(s1,iv),any::seqGet(s2,iv))),all{jv (variable)}(imp(and(leq(Z(0(#)),jv),lt(jv,iv)),equals(any::seqGet(s1,jv),any::seqGet(s2,jv)))))),and(lt(seqLen(s1),seqLen(s2)),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s1))),equals(any::seqGet(s1,iv),any::seqGet(s2,iv))))))) 

Choices: {}}
```

## predecrement

```
predecrement {
\find(#allmodal ( (modal operator))\[{ .. --#lhs1; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1-1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## predecrement_array

```
predecrement_array {
\find(#allmodal ( (modal operator))\[{ .. --#e[#e0]; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#e0) #v0 = #e0;#v[#v0]=(#typeof(#e[#e0]))(#v[#v0]-1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## predecrement_assignment

```
predecrement_assignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=--#lhs1; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1-1);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## predecrement_assignment_array

```
predecrement_assignment_array {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=--#e[#e0]; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#e0) #v0 = #e0;#v[#v0]=(#typeof(#e[#e0]))(#v[#v0]-1);#lhs0=#v[#v0]; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## predecrement_assignment_attribute

```
predecrement_assignment_attribute {
\find(#allmodal ( (modal operator))\[{ .. #lhs=--#e.#attribute; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#v.#attribute=(#typeof(#attribute))(#v.#attribute-1);#lhs=#v.#attribute; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## predecrement_attribute

```
predecrement_attribute {
\find(#allmodal ( (modal operator))\[{ .. --#e.#attribute; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#v.#attribute=(#typeof(#attribute))(#v.#attribute-1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## preincrement

```
preincrement {
\find(#allmodal ( (modal operator))\[{ .. ++#lhs1; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1+1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## preincrement_array

```
preincrement_array {
\find(#allmodal ( (modal operator))\[{ .. ++#e[#e0]; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#e0) #v0 = #e0;#v[#v0]=(#typeof(#e[#e0]))(#v[#v0]+1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## preincrement_assignment

```
preincrement_assignment {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=++#lhs1; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs1=(#typeof(#lhs1))(#lhs1+1);#lhs0=#lhs1; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## preincrement_assignment_array

```
preincrement_assignment_array {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=++#e[#e0]; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#e0 (program Expression))), \new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#typeof(#e0) #v0 = #e0;#v[#v0]=(#typeof(#e[#e0]))(#v[#v0]+1);#lhs0=#v[#v0]; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## preincrement_assignment_attribute

```
preincrement_assignment_attribute {
\find(#allmodal ( (modal operator))\[{ .. #lhs0=++#e.#attribute; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#v.#attribute=(#typeof(#attribute))(#v.#attribute+1);#lhs0=#v.#attribute; ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## preincrement_attribute

```
preincrement_attribute {
\find(#allmodal ( (modal operator))\[{ .. ++#e.#attribute; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v = #e;#v.#attribute=(#typeof(#attribute))(#v.#attribute+1); ... }\] (post)) 
\heuristics(simplify_expression)
Choices: {programRules:Java}}
```

## prod_empty

```
prod_empty {
\find(prod{x (variable)}(FALSE,t))
\replacewith(Z(1(#))) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## prod_one

```
prod_one {
\find(prod{x (variable)}(range,Z(1(#))))
\replacewith(Z(1(#))) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## pullOut

```
pullOut {
\find(t)
\sameUpdateLevel\add [equals(t,sk)]==>[] \replacewith(sk) 
\heuristics(semantics_blasting)
Choices: {}}
```

## pullOutSelect

```
pullOutSelect {
\find(beta::select(h,o,f))
\sameUpdateLevel\add [equals(beta::select(h,o,f),selectSK<<selectSK>>)]==>[] \replacewith(selectSK<<selectSK>>) 
\heuristics(pull_out_select)
Choices: {programRules:Java}}
```

## pullOutbsum1

```
pullOutbsum1 {
\find(geq(bsum{uSub1 (variable)}(i0,i1,t1),t)==>)
\add [equals(bsum{uSub1 (variable)}(i0,i1,t1),sk)]==>[] \replacewith([geq(sk,t)]==>[]) 

Choices: {integerSimplificationRules:full}}
```

## pullOutbsum2

```
pullOutbsum2 {
\find(leq(bsum{uSub1 (variable)}(i0,i1,t1),t)==>)
\add [equals(bsum{uSub1 (variable)}(i0,i1,t1),sk)]==>[] \replacewith([leq(sk,t)]==>[]) 

Choices: {integerSimplificationRules:full}}
```

## pull_out_minus

```
pull_out_neg_1 {
\find(mul(neg(i0),i1))
\replacewith(neg(mul(i0,i1))) 

Choices: {}}
```

## pull_out_minus

```
pull_out_neg_2 {
\find(mul(i0,neg(i1)))
\replacewith(neg(mul(i0,i1))) 

Choices: {}}
```

## qeq_literals

```
qeq_literals {
\find(geq(Z(iz),Z(jz)))
\replacewith(#geq(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## reachAddOne

```
reachAddOne {
\find(reach(h,s,o,o2,add(Z(1(#)),n)))
\varcond(\notFreeIn(ov (variable), n (int term)), \notFreeIn(ov (variable), o2 (java.lang.Object term)), \notFreeIn(ov (variable), o (java.lang.Object term)), \notFreeIn(ov (variable), s (LocSet term)), \notFreeIn(ov (variable), h (Heap term)))
\replacewith(and(and(and(geq(n,Z(neglit(1(#)))),not(equals(o,null))),not(equals(o2,null))),or(and(equals(n,Z(neglit(1(#)))),equals(o,o2)),exists{ov (variable)}(and(reach(h,s,o,ov,n),acc(h,s,ov,o2)))))) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reachAddOne2

```
reachAddOne2 {
\find(reach(h,s,o,o2,add(n,Z(1(#)))))
\varcond(\notFreeIn(ov (variable), n (int term)), \notFreeIn(ov (variable), o2 (java.lang.Object term)), \notFreeIn(ov (variable), o (java.lang.Object term)), \notFreeIn(ov (variable), s (LocSet term)), \notFreeIn(ov (variable), h (Heap term)))
\replacewith(and(and(and(geq(n,Z(neglit(1(#)))),not(equals(o,null))),not(equals(o2,null))),or(and(equals(n,Z(neglit(1(#)))),equals(o,o2)),exists{ov (variable)}(and(reach(h,s,o,ov,n),acc(h,s,ov,o2)))))) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reachDefinition

```
reachDefinition {
\find(reach(h,s,o,o2,n))
\varcond(\notFreeIn(ov (variable), n (int term)), \notFreeIn(ov (variable), o2 (java.lang.Object term)), \notFreeIn(ov (variable), o (java.lang.Object term)), \notFreeIn(ov (variable), s (LocSet term)), \notFreeIn(ov (variable), h (Heap term)))
\replacewith(and(and(and(geq(n,Z(0(#))),not(equals(o,null))),not(equals(o2,null))),or(and(equals(n,Z(0(#))),equals(o,o2)),exists{ov (variable)}(and(reach(h,s,o,ov,sub(n,Z(1(#)))),acc(h,s,ov,o2)))))) 

Choices: {reach:on}}
```

## reachDependenciesAnon

```
reachDependenciesAnon {
\find(reach(anon(h,s2,h2),s,o,o2,n))
\sameUpdateLevel\varcond(\notFreeIn(nv (variable), n (int term)), \notFreeIn(nv (variable), o2 (java.lang.Object term)), \notFreeIn(nv (variable), o (java.lang.Object term)), \notFreeIn(nv (variable), s (LocSet term)), \notFreeIn(nv (variable), h2 (Heap term)), \notFreeIn(nv (variable), s2 (LocSet term)), \notFreeIn(nv (variable), h (Heap term)), \notFreeIn(fv (variable), n (int term)), \notFreeIn(fv (variable), o2 (java.lang.Object term)), \notFreeIn(fv (variable), o (java.lang.Object term)), \notFreeIn(fv (variable), s (LocSet term)), \notFreeIn(fv (variable), h2 (Heap term)), \notFreeIn(fv (variable), s2 (LocSet term)), \notFreeIn(fv (variable), h (Heap term)), \notFreeIn(ov (variable), n (int term)), \notFreeIn(ov (variable), o2 (java.lang.Object term)), \notFreeIn(ov (variable), o (java.lang.Object term)), \notFreeIn(ov (variable), s (LocSet term)), \notFreeIn(ov (variable), h2 (Heap term)), \notFreeIn(ov (variable), s2 (LocSet term)), \notFreeIn(ov (variable), h (Heap term)))
\add [all{ov (variable)}(all{fv (variable)}(not(and(and(elementOf(ov,fv,s2),exists{nv (variable)}(and(lt(nv,n),reach(h,s,o,ov,nv)))),elementOf(ov,fv,s)))))]==>[] \replacewith(reach(h,s,o,o2,n)) ;
\add []==>[all{ov (variable)}(all{fv (variable)}(not(and(and(elementOf(ov,fv,s2),exists{nv (variable)}(and(lt(nv,n),reach(h,s,o,ov,nv)))),elementOf(ov,fv,s)))))] 

Choices: {reach:on}}
```

## reachDependenciesAnonCoarse

```
reachDependenciesAnonCoarse {
\find(reach(anon(h,s2,h2),s,o,o2,n))
\sameUpdateLevel\varcond(\notFreeIn(nv (variable), n (int term)), \notFreeIn(nv (variable), o2 (java.lang.Object term)), \notFreeIn(nv (variable), o (java.lang.Object term)), \notFreeIn(nv (variable), s (LocSet term)), \notFreeIn(nv (variable), h2 (Heap term)), \notFreeIn(nv (variable), s2 (LocSet term)), \notFreeIn(nv (variable), h (Heap term)), \notFreeIn(fv (variable), n (int term)), \notFreeIn(fv (variable), o2 (java.lang.Object term)), \notFreeIn(fv (variable), o (java.lang.Object term)), \notFreeIn(fv (variable), s (LocSet term)), \notFreeIn(fv (variable), h2 (Heap term)), \notFreeIn(fv (variable), s2 (LocSet term)), \notFreeIn(fv (variable), h (Heap term)), \notFreeIn(ov (variable), n (int term)), \notFreeIn(ov (variable), o2 (java.lang.Object term)), \notFreeIn(ov (variable), o (java.lang.Object term)), \notFreeIn(ov (variable), s (LocSet term)), \notFreeIn(ov (variable), h2 (Heap term)), \notFreeIn(ov (variable), s2 (LocSet term)), \notFreeIn(ov (variable), h (Heap term)))
\add [all{ov (variable)}(all{fv (variable)}(not(and(and(elementOf(ov,fv,s2),exists{nv (variable)}(reach(h,s,o,ov,nv))),elementOf(ov,fv,s)))))]==>[] \replacewith(reach(h,s,o,o2,n)) ;
\add []==>[all{ov (variable)}(all{fv (variable)}(not(and(and(elementOf(ov,fv,s2),exists{nv (variable)}(reach(h,s,o,ov,nv))),elementOf(ov,fv,s)))))] 

Choices: {reach:on}}
```

## reachDependenciesStore

```
reachDependenciesStore {
\find(reach(store(h,o3,f,x),s,o,o2,n))
\sameUpdateLevel\varcond(\notFreeIn(nv (variable), n (int term)), \notFreeIn(nv (variable), o2 (java.lang.Object term)), \notFreeIn(nv (variable), o (java.lang.Object term)), \notFreeIn(nv (variable), s (LocSet term)), \notFreeIn(nv (variable), x (any term)), \notFreeIn(nv (variable), f (Field term)), \notFreeIn(nv (variable), o3 (java.lang.Object term)), \notFreeIn(nv (variable), h (Heap term)))
\add [not(and(exists{nv (variable)}(and(lt(nv,n),reach(h,s,o,o3,nv))),elementOf(o3,f,s)))]==>[] \replacewith(reach(h,s,o,o2,n)) ;
\add []==>[not(and(exists{nv (variable)}(and(lt(nv,n),reach(h,s,o,o3,nv))),elementOf(o3,f,s)))] 

Choices: {reach:on}}
```

## reachDependenciesStoreEQ

```
reachDependenciesStoreEQ {
\assumes ([equals(store(h,o3,f,x),h2)]==>[]) 
\find(reach(h2,s,o,o2,n))
\sameUpdateLevel\varcond(\notFreeIn(nv (variable), n (int term)), \notFreeIn(nv (variable), o2 (java.lang.Object term)), \notFreeIn(nv (variable), o (java.lang.Object term)), \notFreeIn(nv (variable), s (LocSet term)), \notFreeIn(nv (variable), x (any term)), \notFreeIn(nv (variable), f (Field term)), \notFreeIn(nv (variable), o3 (java.lang.Object term)), \notFreeIn(nv (variable), h (Heap term)))
\add [not(and(exists{nv (variable)}(and(lt(nv,n),reach(h,s,o,o3,nv))),elementOf(o3,f,s)))]==>[] \replacewith(reach(h,s,o,o2,n)) ;
\add []==>[not(and(exists{nv (variable)}(and(lt(nv,n),reach(h,s,o,o3,nv))),elementOf(o3,f,s)))] 

Choices: {reach:on}}
```

## reachDependenciesStoreSimple

```
reachDependenciesStoreSimple {
\find(reach(store(h,o3,f2,x),allObjects(f),o,o2,n))
\varcond(\metaDisjoint f (Field term), f2 (Field term), )
\replacewith(reach(h,allObjects(f),o,o2,n)) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reachDependenciesStoreSimpleEQ

```
reachDependenciesStoreSimpleEQ {
\assumes ([equals(store(h,o3,f2,x),h2)]==>[]) 
\find(reach(h2,allObjects(f),o,o2,n))
\varcond(\metaDisjoint f (Field term), f2 (Field term), )
\replacewith(reach(h,allObjects(f),o,o2,n)) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reachDoesNotDependOnCreatedness

```
reachDoesNotDependOnCreatedness {
\find(reach(create(h,o3),s,o,o2,n))
\replacewith(reach(h,s,o,o2,n)) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reachEndOfUniquePath

```
reachEndOfUniquePath {
\assumes ([reach(h,allObjects(f),o,o2,n),equals(alpha::select(h,o2,f),null),equals(alpha::select(h,o3,f),null)]==>[]) 
\find(reach(h,allObjects(f),o,o3,n2)==>)
\varcond(\different (n (int term), n2 (int term)), )
\add [and(equals(o2,o3),equals(n,n2))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {reach:on}}
```

## reachEndOfUniquePath2

```
reachEndOfUniquePath2 {
\assumes ([reach(h,allObjects(f),o,o2,n),equals(alpha::select(h,o2,f),null)]==>[]) 
\find(reach(h,allObjects(f),o,o3,n2)==>)
\varcond(\different (o (java.lang.Object term), o2 (java.lang.Object term)), \different (n (int term), n2 (int term)), )
\add [or(lt(n2,n),and(equals(o2,o3),equals(n,n2)))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {reach:on}}
```

## reachNull

```
reachNull {
\find(reach(h,s,o,null,n))
\replacewith(false) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reachNull2

```
reachNull2 {
\find(reach(h,s,null,o2,n))
\replacewith(false) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reachOne

```
reachOne {
\find(reach(h,s,o,o2,Z(1(#))))
\replacewith(acc(h,s,o,o2)) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reachUniquePathSameSteps

```
reachUniquePathSameSteps {
\assumes ([reach(h,allObjects(f),o,o2,n)]==>[]) 
\find(reach(h,allObjects(f),o,o3,n)==>)
\varcond(\different (o2 (java.lang.Object term), o3 (java.lang.Object term)), )
\add [equals(o2,o3)]==>[] 
\heuristics(inReachableStateImplication)
Choices: {reach:on}}
```

## reachZero

```
reachZero {
\find(reach(h,s,o,o2,Z(0(#))))
\replacewith(and(not(equals(o,null)),equals(o,o2))) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reach_does_not_depend_on_fresh_locs

```
reach_does_not_depend_on_fresh_locs {
\assumes ([]==>[equals(o,null)]) 
\find(reach(anon(h,empty,h2),s,o,o2,n))
\add []==>[and(wellFormed(h),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))] ;
\replacewith(reach(h,s,o,o2,n)) 
\heuristics(simplify)
Choices: {reach:on}}
```

## reach_does_not_depend_on_fresh_locs_EQ

```
reach_does_not_depend_on_fresh_locs_EQ {
\assumes ([equals(anon(h,empty,h2),EQ)]==>[equals(o,null)]) 
\find(reach(EQ,s,o,o2,n))
\add []==>[and(wellFormed(h),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))] ;
\replacewith(reach(h,s,o,o2,n)) 
\heuristics(simplify)
Choices: {reach:on}}
```

## readPermission

```
readPermission {
\find(readPermission(p))
\replacewith(readPermissionObject(currentThread,p)) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## readPermissionAfterTransferRead

```
readPermissionAfterTransferRead {
\assumes ([readPermissionObject(o1,p)]==>[]) 
\find(readPermissionObject(o2,transferPermission(split,o1,o2,Z(0(#)),p)))
\replacewith(true) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## readPermissionAfterTransferReadEQ

```
readPermissionAfterTransferReadEQ {
\assumes ([readPermissionObject(o1,p),equals(transferPermission(split,o1,o2,Z(0(#)),p),p1)]==>[]) 
\find(readPermissionObject(o2,p1))
\replacewith(true) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## readPermissionAfterTransferWrite

```
readPermissionAfterTransferWrite {
\assumes ([writePermissionObject(o1,p)]==>[]) 
\find(readPermissionObject(o2,transferPermission(split,o1,o2,Z(0(#)),p)))
\replacewith(true) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## readPermissionAfterTransferWriteEQ

```
readPermissionAfterTransferWriteEQ {
\assumes ([writePermissionObject(o1,p),equals(transferPermission(split,o1,o2,Z(0(#)),p),p1)]==>[]) 
\find(readPermissionObject(o2,p1))
\replacewith(true) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## readPermissionEmpty

```
readPermissionEmpty {
\find(readPermissionObject(o,emptyPermission))
\replacewith(false) 
\heuristics(concrete)
Choices: {permissions:on}}
```

## readPermissionObject

```
readPermissionObject {
\find(readPermissionObject(o,p))
\replacewith(true) 
\heuristics(concrete)
Choices: {permissions:off}}
```

## readPermissionOwe

```
readPermissionOwe {
\find(readPermissionOwe(o1,o2,p))
\varcond(\notFreeIn(pp (variable), o2 (java.lang.Object term)), \notFreeIn(ol (variable), o2 (java.lang.Object term)), \notFreeIn(pp (variable), o1 (java.lang.Object term)), \notFreeIn(ol (variable), o1 (java.lang.Object term)), \notFreeIn(pp (variable), p (Permission term)), \notFreeIn(ol (variable), p (Permission term)))
\replacewith(exists{ol (variable)}(exists{pp (variable)}(equals(p,slice(consPermissionOwnerList(o1,consPermissionOwnerList(o2,ol)),pp))))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## readPermissionOwe2

```
readPermissionOwe2 {
\find(readPermissionOwe2(o1,o2,p))
\varcond(\notFreeIn(pp (variable), o2 (java.lang.Object term)), \notFreeIn(ol (variable), o2 (java.lang.Object term)), \notFreeIn(ol0 (variable), o2 (java.lang.Object term)), \notFreeIn(pp (variable), o1 (java.lang.Object term)), \notFreeIn(ol (variable), o1 (java.lang.Object term)), \notFreeIn(ol0 (variable), o1 (java.lang.Object term)), \notFreeIn(pp (variable), p (Permission term)), \notFreeIn(ol (variable), p (Permission term)), \notFreeIn(ol0 (variable), p (Permission term)))
\replacewith(exists{ol0 (variable)}(exists{ol (variable)}(exists{pp (variable)}(equals(p,slice(ol0,slice(consPermissionOwnerList(o1,consPermissionOwnerList(o2,ol)),pp))))))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## readPermissionSlice

```
readPermissionSlice {
\find(readPermissionObject(o,slice(ol,p)))
\replacewith(or(checkPermissionOwner(o,Z(0(#)),ol),readPermissionObject(o,p))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## reference_type_cast

```
reference_type_cast {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(#npit)#se; ... }\] (post))
\sameUpdateLevel\varcond(\hasSort(#npit (program NonPrimitiveType), G), \not\sub(\typeof(#se (program SimpleExpression)), G), )
\add []==>[or(equals(#se,null),equals(G::instance(#se),TRUE))] \replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.ClassCastException (); ... }\] (post)) ;
\add [or(equals(#se,null),equals(G::instance(#se),TRUE))]==>[] \replacewith(update-application(elem-update(#lhs (program LeftHandSide))(#addCast(#se,#lhs)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## reference_type_cast

```
reference_type_cast {
\find(==>#allmodal ( (modal operator))\[{ .. #lhs=(#npit)#se; ... }\] (post))
\varcond(\hasSort(#npit (program NonPrimitiveType), G), \not\sub(\typeof(#se (program SimpleExpression)), G), )
\add []==>[or(equals(#se,null),equals(G::instance(#se),TRUE))] \replacewith([]==>[false]) ;
\replacewith([]==>[update-application(elem-update(#lhs (program LeftHandSide))(#addCast(#se,#lhs)),#allmodal(post))]) 
\heuristics(simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## reference_type_cast

```
reference_type_cast {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(#npit)#se; ... }\] (post))
\varcond(\hasSort(#npit (program NonPrimitiveType), G), )
\replacewith(update-application(elem-update(#lhs (program LeftHandSide))(#addCast(#se,#lhs)),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## referencedObjectIsCreatedRight

```
referencedObjectIsCreatedRight {
\assumes ([]==>[equals(deltaObject::select(h,o,f),null)]) 
\find(==>equals(boolean::select(h,deltaObject::select(h,o,f),java.lang.Object::<created>),TRUE))
\replacewith([]==>[wellFormed(h)]) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## referencedObjectIsCreatedRightEQ

```
referencedObjectIsCreatedRightEQ {
\assumes ([equals(deltaObject::select(h,o,f),EQ)]==>[equals(EQ,null)]) 
\find(==>equals(boolean::select(h,EQ,java.lang.Object::<created>),TRUE))
\replacewith([]==>[wellFormed(h)]) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## regExAxiom

```
regExAxiom {
\find(match(regEx(stringAsPattern),string))
\replacewith(equals(string,stringAsPattern)) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## regExConcatAltLeft

```
regExConcatAltLeft {
\find(match(regExConcat(alt(rexp1,rexp2),rexp3),string))
\replacewith(or(match(regExConcat(rexp1,rexp3),string),match(regExConcat(rexp2,rexp3),string))) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## regExConcatAltRight

```
regExConcatAltRight {
\find(match(regExConcat(rexp1,alt(rexp2,rexp3)),string))
\replacewith(or(match(regExConcat(rexp1,rexp2),string),match(regExConcat(rexp1,rexp3),string))) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## regExConcatAxiom

```
regExConcatAxiom {
\find(match(regExConcat(rexp1,rexp2),string))
\varcond(\notFreeIn(endIdx (variable), string (Seq term)), \notFreeIn(endIdx (variable), rexp2 (RegEx term)), \notFreeIn(endIdx (variable), rexp1 (RegEx term)))
\replacewith(exists{endIdx (variable)}(and(and(and(geq(endIdx,Z(0(#))),leq(endIdx,seqLen(string))),match(rexp1,seqSub(string,Z(0(#)),endIdx))),match(rexp2,seqSub(string,endIdx,seqLen(string)))))) 
\heuristics(simplify)
Choices: {Strings:on}}
```

## regExConcatConcreteStringLeft

```
regExConcatConcreteStringLeft {
\find(match(regExConcat(regEx(pattern),rexp),string))
\replacewith(and(and(leq(seqLen(pattern),seqLen(string)),match(regEx(pattern),seqSub(string,Z(0(#)),seqLen(pattern)))),match(rexp,seqSub(string,seqLen(pattern),seqLen(string))))) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## regExConcatConcreteStringRight

```
regExConcatConcreteStringRight {
\find(match(regExConcat(rexp,regEx(pattern)),string))
\replacewith(and(and(leq(seqLen(pattern),seqLen(string)),match(rexp,seqSub(string,Z(0(#)),sub(seqLen(string),seqLen(pattern))))),match(regEx(pattern),seqSub(string,sub(seqLen(string),seqLen(pattern)),seqLen(string))))) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## regExConcatOptLeft

```
regExConcatOptLeft {
\find(match(regExConcat(opt(rexp1),rexp2),string))
\replacewith(or(match(rexp2,string),match(regExConcat(rexp1,rexp2),string))) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## regExConcatOptRight

```
regExConcatOptRight {
\find(match(regExConcat(rexp1,opt(rexp2)),string))
\replacewith(or(match(rexp1,string),match(regExConcat(rexp1,rexp2),string))) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## regExConcatRepeatLeft

```
regExConcatRepeatLeft {
\find(match(regExConcat(repeat(rexp1,nTimes),rexp2),string))
\varcond(\notFreeIn(string2 (variable), nTimes (int term)), \notFreeIn(string2 (variable), rexp2 (RegEx term)), \notFreeIn(string2 (variable), rexp1 (RegEx term)), \notFreeIn(string2 (variable), string (Seq term)), \notFreeIn(string1 (variable), nTimes (int term)), \notFreeIn(string1 (variable), rexp2 (RegEx term)), \notFreeIn(string1 (variable), rexp1 (RegEx term)), \notFreeIn(string1 (variable), string (Seq term)))
\replacewith(if-then-else(equals(nTimes,Z(0(#))),match(rexp2,string),if-then-else(lt(nTimes,Z(0(#))),false,exists{string1 (variable)}(exists{string2 (variable)}(and(and(match(repeat(rexp1,nTimes),string1),match(rexp2,string2)),equals(string,seqConcat(string1,string2)))))))) 
\heuristics(simplify)
Choices: {Strings:on}}
```

## regExConcatRepeatRight

```
regExConcatRepeatRight {
\find(match(regExConcat(rexp1,repeat(rexp2,nTimes)),string))
\varcond(\notFreeIn(string2 (variable), nTimes (int term)), \notFreeIn(string2 (variable), rexp2 (RegEx term)), \notFreeIn(string2 (variable), rexp1 (RegEx term)), \notFreeIn(string2 (variable), string (Seq term)), \notFreeIn(string1 (variable), nTimes (int term)), \notFreeIn(string1 (variable), rexp2 (RegEx term)), \notFreeIn(string1 (variable), rexp1 (RegEx term)), \notFreeIn(string1 (variable), string (Seq term)))
\replacewith(if-then-else(equals(nTimes,Z(0(#))),match(rexp1,string),if-then-else(lt(nTimes,Z(0(#))),false,exists{string1 (variable)}(exists{string2 (variable)}(and(and(match(rexp1,string1),match(repeat(rexp2,nTimes),string2)),equals(string,seqConcat(string1,string2)))))))) 
\heuristics(simplify)
Choices: {Strings:on}}
```

## removeZeros

```
removeZeros {
\find(clRemoveZeros(l))
\replacewith(if-then-else(or(equals(l,seqEmpty),equals(int::seqGet(l,Z(0(#))),C(8(4(#))))),l,clRemoveZeros(seqSub(l,Z(1(#)),seqLen(l))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## remove_parentheses

```
remove_parentheses_attribute_left {
\find(#allmodal ( (modal operator))\[{ .. (#e.#attribute)=#e0; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #e.#attribute=#e0; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## remove_parentheses

```
remove_parentheses_lhs_left {
\find(#allmodal ( (modal operator))\[{ .. (#lhs)=#e; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#e; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## remove_parentheses_right

```
remove_parentheses_right {
\find(#allmodal ( (modal operator))\[{ .. #lhs=(#e); ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #lhs=#e; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## repeatAxiom

```
repeatAxiom {
\find(match(repeat(rexp,nTimes),string))
\varcond(\notFreeIn(endIdx (variable), string (Seq term)), \notFreeIn(endIdx (variable), nTimes (int term)), \notFreeIn(endIdx (variable), rexp (RegEx term)))
\replacewith(if-then-else(lt(nTimes,Z(0(#))),false,if-then-else(equals(nTimes,Z(0(#))),equals(string,empty),exists{endIdx (variable)}(and(and(and(geq(endIdx,Z(0(#))),leq(endIdx,seqLen(string))),match(rexp,seqSub(string,Z(0(#)),endIdx))),match(repeat(rexp,sub(nTimes,Z(1(#)))),seqSub(string,endIdx,seqLen(string)))))))) 

Choices: {Strings:on}}
```

## repeatMatchEmpty

```
repeatMatchEmpty {
\find(match(repeat(rexp,nTimes1),seqEmpty))
\replacewith(or(equals(nTimes1,Z(0(#))),and(gt(nTimes1,Z(0(#))),match(rexp,seqEmpty)))) 
\heuristics(simplify)
Choices: {Strings:on}}
```

## repeatOnce

```
repeatOnce {
\find(repeat(rexp,Z(1(#))))
\replacewith(rexp) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## repeatPlusAxiom

```
repeatPlusAxiom {
\find(match(repeatPlus(rexp),string))
\varcond(\notFreeIn(nTimes (variable), string (Seq term)), \notFreeIn(nTimes (variable), rexp (RegEx term)))
\replacewith(exists{nTimes (variable)}(and(geq(nTimes,Z(1(#))),match(repeat(rexp,nTimes),string)))) 
\heuristics(simplify)
Choices: {Strings:on}}
```

## repeatStarAxiom

```
repeatStarAxiom {
\find(match(repeatStar(rexp),string))
\varcond(\notFreeIn(nTimes (variable), string (Seq term)), \notFreeIn(nTimes (variable), rexp (RegEx term)))
\replacewith(exists{nTimes (variable)}(and(geq(nTimes,Z(0(#))),match(repeat(rexp,nTimes),string)))) 
\heuristics(simplify)
Choices: {Strings:on}}
```

## repeatZero

```
repeatZero {
\find(repeat(rexp,Z(0(#))))
\replacewith(regEx(seqEmpty)) 
\heuristics(concrete)
Choices: {Strings:on}}
```

## replaceConcat

```
replaceConcat {
\find(clReplace(seqConcat(leftStr,rightStr),searchChar,replaceChar))
\replacewith(seqConcat(clReplace(leftStr,searchChar,replaceChar),clReplace(rightStr,searchChar,replaceChar))) 
\heuristics(stringsMoveReplaceInside)
Choices: {Strings:on}}
```

## replaceCons

```
replaceCons {
\find(clReplace(seqConcat(seqSingleton(fstChar),str),searchChar,replChar))
\replacewith(if-then-else(equals(searchChar,fstChar),seqConcat(seqSingleton(replChar),clReplace(str,searchChar,replChar)),seqConcat(seqSingleton(fstChar),clReplace(str,searchChar,replChar)))) 
\heuristics(defOpsReplaceInline)
Choices: {Strings:on}}
```

## replaceDef

```
replaceDef {
\find(clReplace(str,searchChar,replChar))
\sameUpdateLevel\varcond(\notFreeIn(pos (variable), replChar (int term)), \notFreeIn(pos (variable), searchChar (int term)), \notFreeIn(pos (variable), str (Seq term)))
\add [and(equals(clReplace(str,searchChar,replChar),newSym),equals(seqDef{pos (variable)}(Z(0(#)),seqLen(str),if-then-else(equals(int::seqGet(str,pos),searchChar),replChar,int::seqGet(str,pos))),newSym))]==>[] 
\heuristics(stringsIntroduceNewSym, defOpsReplace)
Choices: {Strings:on}}
```

## replaceEmpty

```
replaceEmpty {
\find(clReplace(seqEmpty,searchChar,replChar))
\replacewith(seqEmpty) 
\heuristics(stringsSimplify)
Choices: {Strings:on}}
```

## replaceSingleton

```
replaceSingleton {
\find(clReplace(seqSingleton(fstChar),searchChar,replChar))
\replacewith(if-then-else(equals(searchChar,fstChar),seqSingleton(replChar),seqSingleton(fstChar))) 
\heuristics(stringsSimplify)
Choices: {Strings:on}}
```

## replaceSubstring

```
replaceSubstring {
\assumes ([equals(seqSub(str,startIdx,endIdx),subStr)]==>[]) 
\find(clReplace(subStr,searchChar,replaceChar))
\sameUpdateLevel\replacewith(if-then-else(and(and(geq(startIdx,Z(0(#))),geq(endIdx,startIdx)),leq(endIdx,seqLen(str))),seqSub(clReplace(str,searchChar,replaceChar),startIdx,endIdx),clReplace(subStr,searchChar,replaceChar))) 
\heuristics(stringsMoveReplaceInside)
Choices: {Strings:on}}
```

## replace_byte_HALFRANGE

```
replace_byte_HALFRANGE {
\find(byte_HALFRANGE)
\replacewith(Z(8(2(1(#))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_byte_MAX

```
replace_byte_MAX {
\find(byte_MAX)
\replacewith(Z(7(2(1(#))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_byte_MIN

```
replace_byte_MIN {
\find(byte_MIN)
\replacewith(Z(neglit(8(2(1(#)))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_byte_RANGE

```
replace_byte_RANGE {
\find(byte_RANGE)
\replacewith(Z(6(5(2(#))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_char_MAX

```
replace_char_MAX {
\find(char_MAX)
\replacewith(Z(5(3(5(5(6(#))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_char_MIN

```
replace_char_MIN {
\find(char_MIN)
\replacewith(Z(0(#))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_char_RANGE

```
replace_char_RANGE {
\find(char_RANGE)
\replacewith(Z(6(3(5(5(6(#))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_int_HALFRANGE

```
replace_int_HALFRANGE {
\find(int_HALFRANGE)
\replacewith(Z(8(4(6(3(8(4(7(4(1(2(#)))))))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_int_MAX

```
replace_int_MAX {
\find(int_MAX)
\replacewith(Z(7(4(6(3(8(4(7(4(1(2(#)))))))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_int_MIN

```
replace_int_MIN {
\find(int_MIN)
\replacewith(Z(neglit(8(4(6(3(8(4(7(4(1(2(#))))))))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_int_RANGE

```
replace_int_RANGE {
\find(int_RANGE)
\replacewith(Z(6(9(2(7(6(9(4(9(2(4(#)))))))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_known_left

```
replace_known_left {
\assumes ([b]==>[]) 
\find(b)
\sameUpdateLevel\replacewith(true) 
\heuristics(replace_known_left)
Choices: {}}
```

## replace_known_right

```
replace_known_right {
\assumes ([]==>[b]) 
\find(b)
\sameUpdateLevel\replacewith(false) 
\heuristics(replace_known_right)
Choices: {}}
```

## replace_long_HALFRANGE

```
replace_long_HALFRANGE {
\find(long_HALFRANGE)
\replacewith(Z(8(0(8(5(7(7(4(5(8(6(3(0(2(7(3(3(2(2(9(#))))))))))))))))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_long_MAX

```
replace_long_MAX {
\find(long_MAX)
\replacewith(Z(7(0(8(5(7(7(4(5(8(6(3(0(2(7(3(3(2(2(9(#))))))))))))))))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_long_MIN

```
replace_long_MIN {
\find(long_MIN)
\replacewith(Z(neglit(8(0(8(5(7(7(4(5(8(6(3(0(2(7(3(3(2(2(9(#)))))))))))))))))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_long_RANGE

```
replace_long_RANGE {
\find(long_RANGE)
\replacewith(Z(6(1(6(1(5(5(9(0(7(3(7(0(4(4(7(6(4(4(8(1(#)))))))))))))))))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_short_HALFRANGE

```
replace_short_HALFRANGE {
\find(short_HALFRANGE)
\replacewith(Z(8(6(7(2(3(#))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_short_MAX

```
replace_short_MAX {
\find(short_MAX)
\replacewith(Z(7(6(7(2(3(#))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_short_MIN

```
replace_short_MIN {
\find(short_MIN)
\replacewith(Z(neglit(8(6(7(2(3(#)))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## replace_short_RANGE

```
replace_short_RANGE {
\find(short_RANGE)
\replacewith(Z(6(3(5(5(6(#))))))) 
\heuristics(defOps_expandRanges)
Choices: {}}
```

## returnPermissionOwner

```
returnPermissionOwner {
\find(returnPermissionOwner(o,consPermissionOwnerList(owner,ol)))
\add []==>[checkPermissionOwner(o,Z(0(#)),consPermissionOwnerList(owner,ol))] ;
\replacewith(ol) 
\heuristics(simplify_expression)
Choices: {permissions:on}}
```

## returnPermission_empty

```
returnPermission_empty {
\find(returnPermission(from,to,emptyPermission))
\replacewith(emptyPermission) 
\heuristics(concrete)
Choices: {permissions:on}}
```

## returnPermission_slice

```
returnPermission_slice {
\find(returnPermission(from,to,slice(owners,p)))
\replacewith(if-then-else(equals(from,to),slice(owners,p),if-then-else(and(checkPermissionOwner(from,Z(0(#)),owners),checkPermissionOwner(to,Z(1(#)),owners)),slice(returnPermissionOwner(from,owners),returnPermission(from,to,p)),slice(owners,returnPermission(from,to,p))))) 
\heuristics(simplify_expression)
Choices: {permissions:on}}
```

## returnPermission_slice_split

```
returnPermission_slice_split {
\find(returnPermission(from,to,slice(consPermissionOwnerList(from,owners),slice(owners,p))))
\add []==>[and(not(equals(from,to)),checkPermissionOwner(to,Z(0(#)),owners))] ;
\replacewith(slice(owners,returnPermission(from,to,p))) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## returnUnfold

```
returnUnfold {
\find(#allmodal ( (modal operator))\[{ .. return #nse; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v0 = #nse;return #v0; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## rotate_and

```
rotate_and {
\find(and(b,and(c,d)))
\replacewith(and(c,and(b,d))) 

Choices: {}}
```

## rotate_or

```
rotate_or {
\find(or(b,or(c,d)))
\replacewith(or(c,or(b,d))) 

Choices: {}}
```

## rotate_params

```
rotate_params {
\find(add(i,add(i0,i1)))
\replacewith(add(i0,add(i,i1))) 

Choices: {}}
```

## sameTypeFalse

```
sameTypeFalse {
\assumes ([equals(G::exactInstance(x1),TRUE),equals(H::exactInstance(x2),TRUE)]==>[]) 
\find(sameType(x1,x2))
\varcond(\not\same(G, H), )
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## sameTypeTrue

```
sameTypeTrue {
\assumes ([equals(G::exactInstance(x1),TRUE),equals(G::exactInstance(x2),TRUE)]==>[]) 
\find(sameType(x1,x2))
\replacewith(true) 
\heuristics(concrete)
Choices: {}}
```

## same_boxes_left

```
same_boxes_left {
\assumes ([\[{ .. #s ... }\] (post)]==>[]) 
\find(\[{ .. #s ... }\] (post1)==>)
\add [\[{ .. #s ... }\] (and(post,post1))]==>[] 

Choices: {programRules:Java}}
```

## same_boxes_right

```
same_boxes_right {
\assumes ([]==>[\[{ .. #s ... }\] (post)]) 
\find(==>\[{ .. #s ... }\] (post1))
\add []==>[\[{ .. #s ... }\] (or(post,post1))] 

Choices: {programRules:Java}}
```

## same_diamonds_left

```
same_diamonds_left {
\assumes ([\<{ .. #s ... }\> (post)]==>[]) 
\find(\<{ .. #s ... }\> (post1)==>)
\add [\<{ .. #s ... }\> (and(post,post1))]==>[] 

Choices: {programRules:Java}}
```

## same_diamonds_right

```
same_diamonds_right {
\assumes ([]==>[\<{#s}\> (post)]) 
\find(==>\<{#s}\> (post1))
\add []==>[\<{#s}\> (or(post,post1))] 

Choices: {programRules:Java}}
```

## schiffl_lemma_2

```
schiffl_lemma_2 {
\find(seqPerm(s,t)==>)
\varcond(\notFreeIn(y (variable), t (Seq term)), \notFreeIn(y (variable), s (Seq term)), \notFreeIn(x (variable), t (Seq term)), \notFreeIn(x (variable), s (Seq term)), \notFreeIn(r (variable), t (Seq term)), \notFreeIn(r (variable), s (Seq term)), \notFreeIn(iv (variable), t (Seq term)), \notFreeIn(iv (variable), s (Seq term)))
\add [all{x (variable)}(all{y (variable)}(imp(and(and(and(and(and(equals(any::seqGet(s,x),any::seqGet(t,x)),equals(any::seqGet(s,y),any::seqGet(t,y))),leq(Z(0(#)),x)),lt(x,seqLen(s))),leq(Z(0(#)),y)),lt(y,seqLen(s))),exists{r (variable)}(and(and(and(and(equals(seqLen(r),seqLen(s)),seqNPerm(r)),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(any::seqGet(s,iv),any::seqGet(t,int::seqGet(r,iv)))))),equals(int::seqGet(r,x),x)),equals(int::seqGet(r,y),y))))))]==>[] 

Choices: {}}
```

## schiffl_thm_1

```
schiffl_thm_1 {
\varcond(\notFreeIn(idx (variable), t (Seq term)), \notFreeIn(idx (variable), s (Seq term)), \notFreeIn(idx (variable), b (any term)), \notFreeIn(idx (variable), a (any term)), \notFreeIn(idx (variable), y (int term)), \notFreeIn(idx (variable), x (int term)))
\add [imp(and(and(and(and(and(and(seqPerm(s,t),equals(any::seqGet(s,x),any::seqGet(t,x))),equals(any::seqGet(s,y),any::seqGet(t,y))),leq(Z(0(#)),x)),lt(x,seqLen(s))),leq(Z(0(#)),y)),lt(y,seqLen(s))),seqPerm(seqDef{idx (variable)}(Z(0(#)),seqLen(s),if-then-else(equals(idx,y),b,if-then-else(equals(idx,x),a,any::seqGet(s,idx)))),seqDef{idx (variable)}(Z(0(#)),seqLen(s),if-then-else(equals(idx,y),b,if-then-else(equals(idx,x),a,any::seqGet(t,idx))))))]==>[] 

Choices: {}}
```

## secondOfPair

```
secondOfPair {
\find(second(pair(t,t1)))
\replacewith(t1) 
\heuristics(concrete)
Choices: {}}
```

## selectCreatedOfAnon

```
selectCreatedOfAnon {
\find(boolean::select(anon(h,s,h2),o,java.lang.Object::<created>))
\replacewith(if-then-else(equals(boolean::select(h,o,java.lang.Object::<created>),TRUE),TRUE,boolean::select(h2,o,java.lang.Object::<created>))) 
\heuristics(simplify_heap_high_costs)
Choices: {programRules:Java}}
```

## selectCreatedOfAnonAsFormula

```
selectCreatedOfAnonAsFormula {
\find(equals(boolean::select(anon(h,s,h2),o,java.lang.Object::<created>),TRUE))
\replacewith(or(equals(boolean::select(h,o,java.lang.Object::<created>),TRUE),equals(boolean::select(h2,o,java.lang.Object::<created>),TRUE))) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## selectCreatedOfAnonAsFormulaEQ

```
selectCreatedOfAnonAsFormulaEQ {
\assumes ([equals(anon(h,s,h2),EQ)]==>[]) 
\find(equals(boolean::select(EQ,o,java.lang.Object::<created>),TRUE))
\sameUpdateLevel\replacewith(or(equals(boolean::select(h,o,java.lang.Object::<created>),TRUE),equals(boolean::select(h2,o,java.lang.Object::<created>),TRUE))) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## selectCreatedOfAnonEQ

```
selectCreatedOfAnonEQ {
\assumes ([equals(anon(h,s,h2),EQ)]==>[]) 
\find(boolean::select(EQ,o,java.lang.Object::<created>))
\sameUpdateLevel\replacewith(if-then-else(equals(boolean::select(h,o,java.lang.Object::<created>),TRUE),TRUE,boolean::select(h2,o,java.lang.Object::<created>))) 
\heuristics(simplify_heap_high_costs)
Choices: {programRules:Java}}
```

## selectOfAnon

```
selectOfAnon {
\find(beta::select(anon(h,s,h2),o,f))
\replacewith(if-then-else(or(and(elementOf(o,f,s),not(equals(f,java.lang.Object::<created>))),elementOf(o,f,freshLocs(h))),beta::select(h2,o,f),beta::select(h,o,f))) 
\heuristics(semantics_blasting)
Choices: {programRules:Java}}
```

## selectOfAnonEQ

```
selectOfAnonEQ {
\assumes ([equals(anon(h,s,h2),EQ)]==>[]) 
\find(beta::select(EQ,o,f))
\sameUpdateLevel\replacewith(if-then-else(or(and(elementOf(o,f,s),not(equals(f,java.lang.Object::<created>))),elementOf(o,f,freshLocs(h))),beta::select(h2,o,f),beta::select(h,o,f))) 
\heuristics(simplify_heap_high_costs)
Choices: {programRules:Java}}
```

## selectOfCreate

```
selectOfCreate {
\find(beta::select(create(h,o),o2,f))
\replacewith(if-then-else(and(equals(o,o2),not(equals(o,null))),if-then-else(equals(f,java.lang.Object::<created>),beta::cast(TRUE),beta::defaultValue),beta::select(h,o2,f))) ;
\replacewith(if-then-else(and(and(equals(o,o2),not(equals(o,null))),equals(f,java.lang.Object::<created>)),beta::cast(TRUE),beta::select(h,o2,f))) 
\heuristics(semantics_blasting)
Choices: {programRules:Java}}
```

## selectOfCreateEQ

```
selectOfCreateEQ {
\assumes ([equals(create(h,o),EQ)]==>[]) 
\find(beta::select(EQ,o2,f))
\sameUpdateLevel\replacewith(if-then-else(and(equals(o,o2),not(equals(o,null))),if-then-else(equals(f,java.lang.Object::<created>),beta::cast(TRUE),beta::defaultValue),beta::select(h,o2,f))) ;
\replacewith(if-then-else(and(and(equals(o,o2),not(equals(o,null))),equals(f,java.lang.Object::<created>)),beta::cast(TRUE),beta::select(h,o2,f))) 
\heuristics(simplify_heap_high_costs)
Choices: {programRules:Java}}
```

## selectOfMemset

```
selectOfMemset {
\find(beta::select(memset(h,s,x),o,f))
\replacewith(if-then-else(and(elementOf(o,f,s),not(equals(f,java.lang.Object::<created>))),beta::cast(x),beta::select(h,o,f))) 
\heuristics(semantics_blasting)
Choices: {programRules:Java}}
```

## selectOfMemsetEQ

```
selectOfMemsetEQ {
\assumes ([equals(memset(h,s,x),EQ)]==>[]) 
\find(beta::select(EQ,o,f))
\sameUpdateLevel\replacewith(if-then-else(and(elementOf(o,f,s),not(equals(f,java.lang.Object::<created>))),beta::cast(x),beta::select(h,o,f))) 
\heuristics(simplify_heap_high_costs)
Choices: {programRules:Java}}
```

## selectOfStore

```
selectOfStore {
\find(beta::select(store(h,o,f,x),o2,f2))
\replacewith(if-then-else(and(and(equals(o,o2),equals(f,f2)),not(equals(f,java.lang.Object::<created>))),beta::cast(x),beta::select(h,o2,f2))) 
\heuristics(semantics_blasting)
Choices: {programRules:Java}}
```

## selectOfStoreEQ

```
selectOfStoreEQ {
\assumes ([equals(store(h,o,f,x),EQ)]==>[]) 
\find(beta::select(EQ,o2,f2))
\sameUpdateLevel\replacewith(if-then-else(and(and(equals(o,o2),equals(f,f2)),not(equals(f,java.lang.Object::<created>))),beta::cast(x),beta::select(h,o2,f2))) 
\heuristics(simplify_heap_high_costs)
Choices: {programRules:Java}}
```

## seqConcatUnfold

```
seqConcatUnfoldLeft {
\find(#allmodal ( (modal operator))\[{ .. #v=\seq_concat(#nseLeft,#eRight); ... }\] (post))
\varcond(\new(#vLeftNew (program Variable), \typeof(#nseLeft (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseLeft) #vLeftNew = #nseLeft;#v=\seq_concat(#vLeftNew,#eRight); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqConcatUnfold

```
seqConcatUnfoldRight {
\find(#allmodal ( (modal operator))\[{ .. #v=\seq_concat(#seLeft,#nseRight); ... }\] (post))
\varcond(\new(#vRightNew (program Variable), \typeof(#nseRight (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseRight) #vRightNew = #nseRight;#v=\seq_concat(#seLeft,#vRightNew); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqConcatWithEmpty

```
seqConcatWithSeqEmpty1 {
\find(seqConcat(seq,seqEmpty))
\replacewith(seq) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## seqConcatWithEmpty

```
seqConcatWithSeqEmpty2 {
\find(seqConcat(seqEmpty,seq))
\replacewith(seq) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## seqDefOfSeq

```
seqDefOfSeq {
\find(seqDef{u (variable)}(Z(0(#)),x,any::seqGet(s,u)))
\varcond(\notFreeIn(v (variable), s (Seq term)), \notFreeIn(v (variable), x (int term)), \notFreeIn(u (variable), s (Seq term)), \notFreeIn(u (variable), x (int term)))
\replacewith(if-then-else(equals(seqLen(s),x),s,if-then-else(gt(seqLen(s),x),seqSub(s,Z(0(#)),x),seqConcat(s,seqDef{v (variable)}(seqLen(s),x,seqGetOutside))))) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## seqDef_empty

```
seqDef_empty {
\find(seqDef{uSub (variable)}(from,idx,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), idx (int term)), \notFreeIn(uSub (variable), from (int term)))
\replacewith(seqEmpty) ;
\add []==>[leq(idx,from)] 

Choices: {sequences:on}}
```

## seqDef_induction_lower

```
seqDef_induction_lower {
\find(seqDef{uSub (variable)}(from,to,t))
\varcond(\notFreeIn(uSub (variable), to (int term)), \notFreeIn(uSub (variable), from (int term)))
\replacewith(seqConcat(if-then-else(lt(from,to),seqSingleton(subst{uSub (variable)}(from,t)),seqEmpty),seqDef{uSub (variable)}(add(from,Z(1(#))),to,t))) 

Choices: {sequences:on}}
```

## seqDef_induction_lower_concrete

```
seqDef_induction_lower_concrete {
\find(seqDef{uSub (variable)}(add(Z(neglit(1(#))),from),to,t))
\varcond(\notFreeIn(uSub (variable), to (int term)), \notFreeIn(uSub (variable), from (int term)))
\replacewith(seqConcat(if-then-else(lt(add(Z(neglit(1(#))),from),to),seqSingleton(subst{uSub (variable)}(add(Z(neglit(1(#))),from),t)),seqEmpty),seqDef{uSub (variable)}(from,to,t))) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## seqDef_induction_upper

```
seqDef_induction_upper {
\find(seqDef{uSub (variable)}(from,to,t))
\varcond(\notFreeIn(uSub (variable), to (int term)), \notFreeIn(uSub (variable), from (int term)))
\replacewith(seqConcat(seqDef{uSub (variable)}(from,sub(to,Z(1(#))),t),if-then-else(lt(from,to),seqSingleton(subst{uSub (variable)}(sub(to,Z(1(#))),t)),seqEmpty))) 

Choices: {sequences:on}}
```

## seqDef_induction_upper_concrete

```
seqDef_induction_upper_concrete {
\find(seqDef{uSub (variable)}(from,add(Z(1(#)),to),t))
\varcond(\notFreeIn(uSub (variable), to (int term)), \notFreeIn(uSub (variable), from (int term)))
\replacewith(seqConcat(seqDef{uSub (variable)}(from,to,t),if-then-else(leq(from,to),seqSingleton(subst{uSub (variable)}(to,t)),seqEmpty))) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## seqDef_lower_equals_upper

```
seqDef_lower_equals_upper {
\find(seqDef{uSub (variable)}(idx,idx,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), idx (int term)))
\replacewith(seqEmpty) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## seqDef_one_summand

```
seqDef_one_summand {
\find(seqDef{uSub (variable)}(from,idx,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub (variable), idx (int term)), \notFreeIn(uSub (variable), from (int term)))
\replacewith(if-then-else(equals(add(from,Z(1(#))),idx),seqSingleton(subst{uSub (variable)}(from,t)),seqDef{uSub (variable)}(from,idx,t))) 

Choices: {sequences:on}}
```

## seqDef_split

```
seqDef_split {
\find(seqDef{uSub (variable)}(from,to,t))
\varcond(\notFreeIn(uSub1 (variable), t (any term)), \notFreeIn(uSub (variable), to (int term)), \notFreeIn(uSub (variable), idx (int term)), \notFreeIn(uSub (variable), from (int term)), \notFreeIn(uSub1 (variable), to (int term)), \notFreeIn(uSub1 (variable), idx (int term)), \notFreeIn(uSub1 (variable), from (int term)))
\replacewith(if-then-else(and(leq(from,idx),lt(idx,to)),seqConcat(seqDef{uSub (variable)}(from,idx,t),seqDef{uSub1 (variable)}(idx,to,subst{uSub (variable)}(uSub1,t))),seqDef{uSub (variable)}(from,to,t))) 

Choices: {sequences:on}}
```

## seqDef_split_in_three

```
seqDef_split_in_three {
\find(seqDef{uSub (variable)}(from,to,t))
\sameUpdateLevel\varcond(\notFreeIn(uSub1 (variable), to (int term)), \notFreeIn(uSub (variable), from (int term)), \notFreeIn(uSub1 (variable), idx (int term)), \notFreeIn(uSub1 (variable), t (any term)), \notFreeIn(uSub (variable), idx (int term)))
\replacewith(seqConcat(seqDef{uSub (variable)}(from,idx,t),seqConcat(seqSingleton(subst{uSub (variable)}(idx,t)),seqDef{uSub1 (variable)}(add(idx,Z(1(#))),to,subst{uSub (variable)}(uSub1,t))))) ;
\add []==>[and(leq(from,idx),lt(idx,to))] 

Choices: {sequences:on}}
```

## seqGetAlphaCast

```
seqGetAlphaCast {
\find(alpha::seqGet(seq,at))
\add [equals(alpha::cast(any::seqGet(seq,at)),alpha::seqGet(seq,at))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {sequences:on}}
```

## seqGetSInvS

```
seqGetSInvS {
\find(int::seqGet(s,int::seqGet(seqNPermInv(s),t)))
\add []==>[and(and(seqNPerm(s),leq(Z(0(#)),t)),lt(t,seqLen(s)))] ;
\replacewith(t) 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqGetUnfold

```
seqGetUnfoldLeft {
\find(#allmodal ( (modal operator))\[{ .. #v=#nseLeft[#eRight]; ... }\] (post))
\varcond(\new(#vLeftNew (program Variable), \typeof(#nseLeft (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseLeft) #vLeftNew = #nseLeft;#v=#vLeftNew[#eRight]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqGetUnfold

```
seqGetUnfoldRight {
\find(#allmodal ( (modal operator))\[{ .. #v=#seLeft[#nseRight]; ... }\] (post))
\varcond(\new(#vRightNew (program Variable), \typeof(#nseRight (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseRight) #vRightNew = #nseRight;#v=#seLeft[#vRightNew]; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqIndexOf

```
seqIndexOf {
\find(seqIndexOf(s,t))
\varcond(\notFreeIn(m (variable), t (any term)), \notFreeIn(m (variable), s (Seq term)), \notFreeIn(n (variable), t (any term)), \notFreeIn(n (variable), s (Seq term)))
\add [imp(exists{n (variable)}(and(and(leq(Z(0(#)),n),lt(n,seqLen(s))),equals(any::seqGet(s,n),t))),and(and(and(leq(Z(0(#)),seqIndexOf(s,t)),lt(seqIndexOf(s,t),seqLen(s))),equals(any::seqGet(s,seqIndexOf(s,t)),t)),all{m (variable)}(imp(and(leq(Z(0(#)),m),lt(m,seqIndexOf(s,t))),not(equals(any::seqGet(s,m),t))))))]==>[] 

Choices: {sequences:on}}
```

## seqIndexOfUnfold

```
seqIndexOfUnfoldLeft {
\find(#allmodal ( (modal operator))\[{ .. #v=\indexOf(#nseLeft,#eRight); ... }\] (post))
\varcond(\new(#vLeftNew (program Variable), \typeof(#nseLeft (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseLeft) #vLeftNew = #nseLeft;#v=\indexOf(#vLeftNew,#eRight); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqIndexOfUnfold

```
seqIndexOfUnfoldRight {
\find(#allmodal ( (modal operator))\[{ .. #v=\indexOf(#seLeft,#nseRight); ... }\] (post))
\varcond(\new(#vRightNew (program Variable), \typeof(#nseRight (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseRight) #vRightNew = #nseRight;#v=\indexOf(#seLeft,#vRightNew); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqLengthUnfold

```
seqLengthUnfold {
\find(#allmodal ( (modal operator))\[{ .. #v=#nse.length; ... }\] (post))
\varcond(\new(#vNew (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #vNew = #nse;#v=#vNew.length; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqNPermComp

```
seqNPermComp {
\assumes ([and(seqNPerm(s2),equals(seqLen(s1),seqLen(s2)))]==>[]) 
\find(seqNPerm(s1)==>)
\varcond(\notFreeIn(u (variable), s2 (Seq term)), \notFreeIn(u (variable), s1 (Seq term)))
\add [seqNPerm(seqDef{u (variable)}(Z(0(#)),seqLen(s1),int::seqGet(s1,int::seqGet(s2,u))))]==>[] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermDefLeft

```
seqNPermDefLeft {
\find(seqNPerm(s1)==>)
\varcond(\notFreeIn(jv (variable), s1 (Seq term)), \notFreeIn(iv (variable), s1 (Seq term)))
\add [all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s1))),exists{jv (variable)}(and(and(leq(Z(0(#)),jv),lt(jv,seqLen(s1))),equals(any::seqGet(s1,jv),iv)))))]==>[] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermDefReplace

```
seqNPermDefReplace {
\find(seqNPerm(s1))
\varcond(\notFreeIn(jv (variable), s1 (Seq term)), \notFreeIn(iv (variable), s1 (Seq term)))
\replacewith(all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s1))),exists{jv (variable)}(and(and(leq(Z(0(#)),jv),lt(jv,seqLen(s1))),equals(any::seqGet(s1,jv),iv)))))) 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermEmpty

```
seqNPermEmpty {
\find(seqNPerm(seqEmpty))
\replacewith(true) 
\heuristics(concrete)
Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermInjective

```
seqNPermInjective {
\find(seqNPerm(s)==>)
\varcond(\notFreeIn(jv (variable), s (Seq term)), \notFreeIn(iv (variable), s (Seq term)))
\add [all{iv (variable)}(all{jv (variable)}(imp(and(and(and(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),leq(Z(0(#)),jv)),lt(jv,seqLen(s))),equals(int::seqGet(s,iv),int::seqGet(s,jv))),equals(iv,jv))))]==>[] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermInvNPermLeft

```
seqNPermInvNPermLeft {
\find(seqNPerm(s1)==>)
\add [seqNPerm(seqNPermInv(s1))]==>[] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermInvNPermReplace

```
seqNPermInvNPermReplace {
\find(seqNPerm(seqNPermInv(s1)))
\replacewith(seqNPerm(s1)) 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermRange

```
seqNPermRange {
\find(seqNPerm(s)==>)
\varcond(\notFreeIn(iv (variable), s (Seq term)))
\add [all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),and(and(leq(Z(0(#)),int::seqGet(s,iv)),lt(int::seqGet(s,iv),seqLen(s))),equals(int::instance(any::seqGet(s,iv)),TRUE))))]==>[] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermRight

```
seqNPermRight {
\find(==>seqNPerm(s))
\varcond(\notFreeIn(jv (variable), s (Seq term)), \notFreeIn(iv (variable), s (Seq term)))
\add []==>[and(and(all{iv (variable)}(all{jv (variable)}(imp(and(and(and(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),leq(Z(0(#)),jv)),lt(jv,seqLen(s))),equals(int::seqGet(s,iv),int::seqGet(s,jv))),equals(iv,jv)))),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),and(leq(Z(0(#)),int::seqGet(s,iv)),lt(int::seqGet(s,iv),seqLen(s)))))),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(int::instance(any::seqGet(s,iv)),TRUE))))] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermSingleton

```
seqNPermSingleton {
\find(seqNPerm(seqSingleton(si)))
\replacewith(equals(si,Z(0(#)))) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermSingletonConrete

```
seqNPermSingletonConrete {
\find(seqNPerm(seqSingleton(Z(0(#)))))
\replacewith(true) 
\heuristics(concrete)
Choices: {moreSeqRules:on,sequences:on}}
```

## seqNPermSwapNPerm

```
seqNPermSwapNPerm {
\find(seqNPerm(s1)==>)
\varcond(\notFreeIn(jv (variable), s1 (Seq term)), \notFreeIn(iv (variable), s1 (Seq term)))
\add [all{iv (variable)}(all{jv (variable)}(imp(and(and(and(leq(Z(0(#)),iv),leq(Z(0(#)),jv)),lt(iv,seqLen(s1))),lt(jv,seqLen(s1))),seqNPerm(seqSwap(s1,iv,jv)))))]==>[] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqOutsideValue

```
seqOutsideValue {
\find(seqGetOutside)
\add [all{s (variable)}(all{iv (variable)}(imp(or(lt(iv,Z(0(#))),leq(seqLen(s),iv)),equals(any::seqGet(s,iv),seqGetOutside))))]==>[] 

Choices: {sequences:on}}
```

## seqPermConcatBW

```
seqPermConcatBW {
\assumes ([seqPerm(s1,t1)]==>[]) 
\find(==>seqPerm(seqConcat(s1,s2),seqConcat(t1,t2)))
\replacewith([]==>[seqPerm(s2,t2)]) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## seqPermConcatFW

```
seqPermConcatFW {
\assumes ([seqPerm(s1,t1)]==>[]) 
\find(seqPerm(s2,t2)==>)
\add [seqPerm(seqConcat(s1,s2),seqConcat(t1,t2))]==>[] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqPermDef

```
seqPermDef {
\find(seqPerm(s1,s2))
\varcond(\notFreeIn(s (variable), s2 (Seq term)), \notFreeIn(s (variable), s1 (Seq term)), \notFreeIn(iv (variable), s2 (Seq term)), \notFreeIn(iv (variable), s1 (Seq term)))
\replacewith(and(equals(seqLen(s1),seqLen(s2)),exists{s (variable)}(and(and(equals(seqLen(s),seqLen(s1)),seqNPerm(s)),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(any::seqGet(s1,iv),any::seqGet(s2,int::seqGet(s,iv))))))))) 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqPermDefLeft

```
seqPermDefLeft {
\find(seqPerm(s1,s2)==>)
\varcond(\notFreeIn(s (variable), s2 (Seq term)), \notFreeIn(s (variable), s1 (Seq term)), \notFreeIn(iv (variable), s2 (Seq term)), \notFreeIn(iv (variable), s1 (Seq term)))
\add [and(equals(seqLen(s1),seqLen(s2)),exists{s (variable)}(and(and(equals(seqLen(s),seqLen(s1)),seqNPerm(s)),all{iv (variable)}(imp(and(leq(Z(0(#)),iv),lt(iv,seqLen(s))),equals(any::seqGet(s1,iv),any::seqGet(s2,int::seqGet(s,iv))))))))]==>[] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqPermEmpty

```
seqPermEmpty1 {
\find(seqPerm(seqEmpty,s))
\replacewith(equals(seqEmpty,s)) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## seqPermEmpty

```
seqPermEmpty2 {
\find(seqPerm(s,seqEmpty))
\replacewith(equals(seqEmpty,s)) 
\heuristics(simplify)
Choices: {moreSeqRules:on,sequences:on}}
```

## seqPermFromSwap

```
seqPermFromSwap {
\assumes ([seqPerm(s1,t1)]==>[]) 
\find(==>seqPerm(s2,t2))
\varcond(\notFreeIn(jv (variable), t2 (Seq term)), \notFreeIn(jv (variable), t1 (Seq term)), \notFreeIn(jv (variable), s2 (Seq term)), \notFreeIn(jv (variable), s1 (Seq term)), \notFreeIn(iv (variable), t2 (Seq term)), \notFreeIn(iv (variable), t1 (Seq term)), \notFreeIn(iv (variable), s2 (Seq term)), \notFreeIn(iv (variable), s1 (Seq term)))
\replacewith([]==>[and(equals(t1,t2),exists{iv (variable)}(exists{jv (variable)}(and(and(and(and(leq(Z(0(#)),iv),leq(Z(0(#)),jv)),lt(iv,seqLen(s2))),lt(jv,seqLen(s2))),equals(s1,seqSwap(s2,iv,jv))))))]) 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqPermRefl

```
seqPermRefl {
\find(seqPerm(s,s))
\replacewith(true) 
\heuristics(concrete)
Choices: {moreSeqRules:on,sequences:on}}
```

## seqPermSym

```
seqPermSym {
\find(seqPerm(s1,s2))
\replacewith(seqPerm(s2,s1)) 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqPermTrans

```
seqPermTrans {
\assumes ([seqPerm(s2,s3)]==>[]) 
\find(seqPerm(s1,s2)==>)
\add [seqPerm(s1,s3)]==>[] 

Choices: {moreSeqRules:on,sequences:on}}
```

## seqReverseOfSeqEmpty

```
seqReverseOfSeqEmpty {
\find(seqReverse(seqEmpty))
\replacewith(seqEmpty) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## seqReverseUnfold

```
seqReverseUnfold {
\find(#allmodal ( (modal operator))\[{ .. #v=\seq_reverse(#nse); ... }\] (post))
\varcond(\new(#vNew (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #vNew = #nse;#v=\seq_reverse(#vNew); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqSelfDefinition

```
seqSelfDefinition {
\find(seq)
\add [all{s (variable)}(equals(s,seqDef{u (variable)}(Z(0(#)),seqLen(s),any::seqGet(s,u))))]==>[] 

Choices: {sequences:on}}
```

## seqSelfDefinition

```
seqSelfDefinitionEQ2 {
\assumes ([equals(seqLen(s),x)]==>[]) 
\find(seqDef{u (variable)}(Z(0(#)),x,any::seqGet(s,u)))
\sameUpdateLevel\varcond(\notFreeIn(u (variable), s (Seq term)), \notFreeIn(u (variable), x (int term)))
\replacewith(s) 
\heuristics(simplify)
Choices: {sequences:on}}
```

## seqSingletonUnfold

```
seqSingletonUnfold {
\find(#allmodal ( (modal operator))\[{ .. #v=\seq_singleton(#nse); ... }\] (post))
\varcond(\new(#vNew (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #vNew = #nse;#v=\seq_singleton(#vNew); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqSubUnfold

```
seqSubUnfoldLeft {
\find(#allmodal ( (modal operator))\[{ .. #v=\seq_sub(#nseLeft,#eMiddle,#eRight); ... }\] (post))
\varcond(\new(#vLeftNew (program Variable), \typeof(#nseLeft (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseLeft) #vLeftNew = #nseLeft;#v=\seq_sub(#vLeftNew,#eMiddle,#eRight); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqSubUnfold

```
seqSubUnfoldMiddle {
\find(#allmodal ( (modal operator))\[{ .. #v=\seq_sub(#seLeft,#nseMiddle,#eRight); ... }\] (post))
\varcond(\new(#vMiddleNew (program Variable), \typeof(#nseMiddle (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseMiddle) #vMiddleNew = #nseMiddle;#v=\seq_sub(#seLeft,#vMiddleNew,#eRight); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqSubUnfold

```
seqSubUnfoldRight {
\find(#allmodal ( (modal operator))\[{ .. #v=\seq_sub(#seLeft,#seMiddle,#nseRight); ... }\] (post))
\varcond(\new(#vRightNew (program Variable), \typeof(#nseRight (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseRight) #vRightNew = #nseRight;#v=\seq_sub(#seLeft,#seMiddle,#vRightNew); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## seqnormalizeDef

```
seqnormalizeDef {
\find(seqDef{u (variable)}(le,ri,t))
\varcond(\notFreeIn(u (variable), ri (int term)), \notFreeIn(u (variable), le (int term)))
\replacewith(if-then-else(lt(le,ri),seqDef{u (variable)}(Z(0(#)),sub(ri,le),subst{u (variable)}(add(u,le),t)),seqEmpty)) 

Choices: {moreSeqRules:on,sequences:on}}
```

## sequentialToParallel1

```
sequentialToParallel1 {
\find(update-application(u,update-application(u2,t)))
\replacewith(update-application(parallel-upd(u,update-application(u,u2)),t)) 
\heuristics(update_join)
Choices: {}}
```

## sequentialToParallel2

```
sequentialToParallel2 {
\find(update-application(u,update-application(u2,phi)))
\replacewith(update-application(parallel-upd(u,update-application(u,u2)),phi)) 
\heuristics(update_join)
Choices: {}}
```

## sequentialToParallel3

```
sequentialToParallel3 {
\find(update-application(u,update-application(u2,u3)))
\replacewith(update-application(parallel-upd(u,update-application(u,u2)),u3)) 
\heuristics(update_join)
Choices: {}}
```

## setIntersectUnfold

```
setIntersectUnfoldLeft {
\find(#allmodal ( (modal operator))\[{ .. #v=\intersect(#nseLeft,#eRight); ... }\] (post))
\varcond(\new(#vLeftNew (program Variable), \typeof(#nseLeft (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseLeft) #vLeftNew = #nseLeft;#v=\intersect(#vLeftNew,#eRight); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## setIntersectUnfold

```
setIntersectUnfoldRight {
\find(#allmodal ( (modal operator))\[{ .. #v=\intersect(#seLeft,#nseRight); ... }\] (post))
\varcond(\new(#vRightNew (program Variable), \typeof(#nseRight (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseRight) #vRightNew = #nseRight;#v=\intersect(#seLeft,#vRightNew); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## setJavaCardTransient

```
setJavaCardTransient {
\find(==>#allmodal ( (modal operator))\[{ .. 
  #jcsystemType.#setTransient(#se,#se1)@#jcsystemType; ... }\] (post))
\replacewith([]==>[not(equals(#se,null))]) ;
\replacewith([]==>[update-application(elem-update(heap)(store(heap,#se,java.lang.Object::<transient>,#se1)),#allmodal(post))]) 
\heuristics(simplify_prog)
Choices: {JavaCard:on,programRules:Java}}
```

## setMinusItself

```
setMinusItself {
\find(setMinus(s,s))
\replacewith(empty) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## setMinusOfUnion

```
setMinusOfUnion {
\find(setMinus(union(s,s2),s3))
\replacewith(union(setMinus(s,s3),setMinus(s2,s3))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## setMinusOfUnionEQ

```
setMinusOfUnionEQ {
\assumes ([equals(union(s,s2),EQ)]==>[]) 
\find(setMinus(EQ,s3))
\sameUpdateLevel\replacewith(union(setMinus(s,s3),setMinus(s2,s3))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## setMinusSingleton

```
setMinusSingleton {
\assumes ([]==>[elementOf(o,f,s)]) 
\find(setMinus(s,singleton(o,f)))
\replacewith(s) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## setMinusUnfold

```
setMinusUnfoldLeft {
\find(#allmodal ( (modal operator))\[{ .. #v=\set_minus(#nseLeft,#eRight); ... }\] (post))
\varcond(\new(#vLeftNew (program Variable), \typeof(#nseLeft (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseLeft) #vLeftNew = #nseLeft;#v=\set_minus(#vLeftNew,#eRight); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## setMinusUnfold

```
setMinusUnfoldRight {
\find(#allmodal ( (modal operator))\[{ .. #v=\set_minus(#seLeft,#nseRight); ... }\] (post))
\varcond(\new(#vRightNew (program Variable), \typeof(#nseRight (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseRight) #vRightNew = #nseRight;#v=\set_minus(#seLeft,#vRightNew); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## setMinusWithAllLocs

```
setMinusWithAllLocs {
\find(setMinus(s,allLocs))
\replacewith(empty) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## setMinusWithEmpty

```
setMinusWithEmpty1 {
\find(setMinus(s,empty))
\replacewith(s) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## setMinusWithEmpty

```
setMinusWithEmpty2 {
\find(setMinus(empty,s))
\replacewith(empty) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## setUnionUnfold

```
setUnionUnfoldLeft {
\find(#allmodal ( (modal operator))\[{ .. #v=\set_union(#nseLeft,#eRight); ... }\] (post))
\varcond(\new(#vLeftNew (program Variable), \typeof(#nseLeft (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseLeft) #vLeftNew = #nseLeft;#v=\set_union(#vLeftNew,#eRight); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## setUnionUnfold

```
setUnionUnfoldRight {
\find(#allmodal ( (modal operator))\[{ .. #v=\set_union(#seLeft,#nseRight); ... }\] (post))
\varcond(\new(#vRightNew (program Variable), \typeof(#nseRight (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseRight) #vRightNew = #nseRight;#v=\set_union(#seLeft,#vRightNew); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## shift

```
shiftright_literals {
\find(shiftright(Z(iz),Z(jz)))
\replacewith(#ShiftRight(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## shift

```
shiftleft_literals {
\find(shiftleft(Z(iz),Z(jz)))
\replacewith(#ShiftLeft(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## shift

```
compound_shiftright_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse>>#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v>>#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## shift

```
compound_shiftright_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e>>#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0>>#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## shift

```
compound_unsigned_shiftright_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse>>>#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v>>>#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## shift

```
compound_unsigned_shiftright_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e>>>#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0>>>#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## shift

```
compound_shiftleft_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse<<#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v<<#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## shift

```
compound_shiftleft_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e<<#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0<<#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## shift

```
assignmentShiftRightInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0>>#se; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaShiftRightInt(#seCharByteShortInt0,#se)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## shift

```
assignmentShiftRightLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong0>>#se; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaShiftRightLong(#seLong0,#se)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## shift

```
assignmentShiftLeftInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0<<#se; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaShiftLeftInt(#seCharByteShortInt0,#se)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## shift

```
assignmentShiftLeftLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong0<<#se; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaShiftLeftLong(#seLong0,#se)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## shift

```
assignmentUnsignedShiftRightInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0>>>#se; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaUnsignedShiftRightInt(#seCharByteShortInt0,#se)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## shift

```
assignmentUnsignedShiftRightLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong0>>>#se; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaUnsignedShiftRightLong(#seLong0,#se)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## shiftLeftDef

```
shiftLeftDef {
\find(shiftleft(left,right))
\replacewith(if-then-else(lt(right,Z(0(#))),shiftright(left,neg(right)),mul(left,pow(Z(2(#)),right)))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## shiftRightDef

```
shiftRightDef {
\find(shiftright(left,right))
\replacewith(if-then-else(lt(right,Z(0(#))),shiftleft(left,neg(right)),div(left,pow(Z(2(#)),right)))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## shift_paren_and

```
shift_paren_and {
\find(and(assoc0,and(assoc1,assoc2)))
\replacewith(and(and(assoc0,assoc1),assoc2)) 
\heuristics(cnf_andAssoc, conjNormalForm)
Choices: {}}
```

## shift_paren_or

```
shift_paren_or {
\find(or(assoc0,or(assoc1,assoc2)))
\replacewith(or(or(assoc0,assoc1),assoc2)) 
\heuristics(cnf_orAssoc, conjNormalForm)
Choices: {}}
```

## sign_case_distinction

```
sign_case_distinction {
\add [geq(signCasesLeft,Z(1(#)))]==>[] ;
\add [equals(signCasesLeft,Z(0(#)))]==>[] ;
\add [leq(signCasesLeft,Z(neglit(1(#))))]==>[] 
\heuristics(inEqSimp_signCases)
Choices: {}}
```

## simplifyIfThenElse

```
simplifyIfThenElseUpdate1 {
\find(if-then-else(phi,update-application(u1,t),update-application(u2,t)))
\varcond(de.uka.ilkd.key.rule.conditions.SimplifyIfThenElseUpdateCondition@8f4b803, )
\replacewith(result) 

Choices: {}}
```

## simplifyIfThenElse

```
simplifyIfThenElseUpdate2 {
\find(if-then-else(phi,t,update-application(u2,t)))
\varcond(de.uka.ilkd.key.rule.conditions.SimplifyIfThenElseUpdateCondition@42f9873e, )
\replacewith(result) 

Choices: {}}
```

## simplifyIfThenElse

```
simplifyIfThenElseUpdate3 {
\find(if-then-else(phi,update-application(u1,t),t))
\varcond(de.uka.ilkd.key.rule.conditions.SimplifyIfThenElseUpdateCondition@18483b8b, )
\replacewith(result) 

Choices: {}}
```

## simplifyIfThenElse

```
simplifyIfThenElseUpdate4 {
\find(if-then-else(phi,t,t))
\varcond(de.uka.ilkd.key.rule.conditions.SimplifyIfThenElseUpdateCondition@24fc2c80, )
\replacewith(result) 

Choices: {}}
```

## simplifySelectOfAnon

```
simplifySelectOfAnon {
\find(equals(beta::select(anon(h,s,h2),o,f),sk)==>)
\addrules [replaceKnownSelect {
\find(beta::select(anon(h,s,h2),o,f))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(or(and(elementOf(o,f,s),not(equals(f,java.lang.Object::<created>))),elementOf(o,f,freshLocs(h))),beta::select(h2,o,f),beta::select(h,o,f)),sk)]==>[]) 
\heuristics(simplify_select)
Choices: {programRules:Java}}
```

## simplifySelectOfAnonEQ

```
simplifySelectOfAnonEQ {
\assumes ([equals(anon(h,s,h2),EQ)]==>[]) 
\find(equals(beta::select(EQ,o,f),sk)==>)
\addrules [replaceKnownSelect {
\find(beta::select(EQ,o,f))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(or(and(elementOf(o,f,s),not(equals(f,java.lang.Object::<created>))),elementOf(o,f,freshLocs(h))),beta::select(h2,o,f),beta::select(h,o,f)),sk)]==>[]) 
\heuristics(simplify_select)
Choices: {programRules:Java}}
```

## simplifySelectOfCreate

```
simplifySelectOfCreate {
\find(equals(beta::select(create(h,o),o2,f),sk)==>)
\addrules [replaceKnownSelect {
\find(beta::select(create(h,o),o2,f))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(and(equals(o,o2),not(equals(o,null))),if-then-else(equals(f,java.lang.Object::<created>),beta::cast(TRUE),beta::defaultValue),beta::select(h,o2,f)),sk)]==>[]) ;
\addrules [replaceKnownSelect {
\find(beta::select(create(h,o),o2,f))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(and(and(equals(o,o2),not(equals(o,null))),equals(f,java.lang.Object::<created>)),beta::cast(TRUE),beta::select(h,o2,f)),sk)]==>[]) 
\heuristics(simplify_select)
Choices: {programRules:Java}}
```

## simplifySelectOfCreateEQ

```
simplifySelectOfCreateEQ {
\assumes ([equals(create(h,o),EQ)]==>[]) 
\find(equals(beta::select(EQ,o2,f),sk)==>)
\addrules [replaceKnownSelect {
\find(beta::select(EQ,o2,f))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(and(equals(o,o2),not(equals(o,null))),if-then-else(equals(f,java.lang.Object::<created>),beta::cast(TRUE),beta::defaultValue),beta::select(h,o2,f)),sk)]==>[]) ;
\addrules [replaceKnownSelect {
\find(beta::select(EQ,o2,f))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(and(and(equals(o,o2),not(equals(o,null))),equals(f,java.lang.Object::<created>)),beta::cast(TRUE),beta::select(h,o2,f)),sk)]==>[]) 
\heuristics(simplify_select)
Choices: {programRules:Java}}
```

## simplifySelectOfMemset

```
simplifySelectOfMemset {
\find(equals(beta::select(memset(h,s,x),o,f),sk)==>)
\addrules [replaceKnownSelect {
\find(beta::select(memset(h,s,x),o,f))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(and(elementOf(o,f,s),not(equals(f,java.lang.Object::<created>))),x,beta::select(h,o,f)),sk)]==>[]) 
\heuristics(simplify_select)
Choices: {programRules:Java}}
```

## simplifySelectOfMemsetEQ

```
simplifySelectOfMemsetEQ {
\assumes ([equals(memset(h,s,x),EQ)]==>[]) 
\find(equals(beta::select(EQ,o,f),sk)==>)
\addrules [replaceKnownSelect {
\find(beta::select(EQ,o,f))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(and(elementOf(o,f,s),not(equals(f,java.lang.Object::<created>))),x,beta::select(h,o,f)),sk)]==>[]) 
\heuristics(simplify_select)
Choices: {programRules:Java}}
```

## simplifySelectOfStore

```
simplifySelectOfStore {
\find(equals(beta::select(store(h,o,f,x),o2,f2),sk)==>)
\addrules [replaceKnownSelect {
\find(beta::select(store(h,o,f,x),o2,f2))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(and(and(equals(o,o2),equals(f,f2)),not(equals(f,java.lang.Object::<created>))),beta::cast(x),beta::select(h,o2,f2)),sk)]==>[]) 
\heuristics(simplify_select)
Choices: {programRules:Java}}
```

## simplifySelectOfStoreEQ

```
simplifySelectOfStoreEQ {
\assumes ([equals(store(h,o,f,x),EQ)]==>[]) 
\find(equals(beta::select(EQ,o2,f2),sk)==>)
\addrules [replaceKnownSelect {
\find(beta::select(EQ,o2,f2))
\inSequentState\replacewith(sk) 
\heuristics(concrete)
Choices: {}}] \replacewith([equals(if-then-else(and(and(equals(o,o2),equals(f,f2)),not(equals(f,java.lang.Object::<created>))),beta::cast(x),beta::select(h,o2,f2)),sk)]==>[]) 
\heuristics(simplify_select)
Choices: {programRules:Java}}
```

## simplifyUpdate1

```
simplifyUpdate1 {
\find(update-application(u,t))
\varcond(\dropEffectlessElementaries(u (update), t (any term), result (any term)), )
\replacewith(result) 
\heuristics(update_elim)
Choices: {}}
```

## simplifyUpdate2

```
simplifyUpdate2 {
\find(update-application(u,phi))
\varcond(\dropEffectlessElementaries(u (update), phi (formula), result (formula)), )
\replacewith(result) 
\heuristics(update_elim)
Choices: {}}
```

## simplifyUpdate3

```
simplifyUpdate3 {
\find(update-application(u,u2))
\varcond(\dropEffectlessElementaries(u (update), u2 (update), result (update)), )
\replacewith(result) 
\heuristics(update_elim)
Choices: {}}
```

## singletonAssignment

```
singletonAssignment {
\find(#allmodal ( (modal operator))\[{ .. #v=\singleton(#seObj.#a); ... }\] (post))
\replacewith(update-application(elem-update(#v (program Variable))(singleton(#seObj,#memberPVToField(#a))),#allmodal(post))) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## singletonEqualsEmpty

```
singletonEqualsEmpty {
\find(equals(singleton(o,f),empty))
\replacewith(false) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## singletonUnfold

```
singletonUnfold {
\find(#allmodal ( (modal operator))\[{ .. #v=\singleton(#nseObj.#a); ... }\] (post))
\varcond(\new(#vObjNew (program Variable), \typeof(#nseObj (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nseObj) #vObjNew = #nseObj;#v=\singleton(#vObjNew.#a); ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## sizeOfMapEmpty

```
sizeOfMapEmpty {
\find(mapSize(mapEmpty))
\sameUpdateLevel\replacewith(Z(0(#))) 
\heuristics(simplify)
Choices: {}}
```

## sizeOfMapRemove

```
sizeOfMapRemove {
\find(mapSize(mapRemove(m,key)))
\add [imp(isFinite(m),equals(mapSize(mapRemove(m,key)),if-then-else(inDomain(m,key),sub(mapSize(m),Z(1(#))),mapSize(m))))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {}}
```

## sizeOfMapSingleton

```
sizeOfMapSingleton {
\find(mapSize(mapSingleton(key,value)))
\sameUpdateLevel\replacewith(Z(1(#))) 
\heuristics(simplify)
Choices: {}}
```

## sizeOfMapUpdate

```
sizeOfMapUpdate {
\find(mapSize(mapUpdate(m,key,value)))
\add [imp(isFinite(m),equals(mapSize(mapUpdate(m,key,value)),if-then-else(inDomain(m,key),mapSize(m),add(mapSize(m),Z(1(#))))))]==>[] 
\heuristics(inReachableStateImplication)
Choices: {}}
```

## sizeOfSeq2Map

```
sizeOfSeq2Map {
\find(mapSize(seq2map(s)))
\sameUpdateLevel\replacewith(seqLen(s)) 
\heuristics(simplify)
Choices: {}}
```

## skip assert

```
skipAssert {
\find(#allmodal ( (modal operator))\[{ .. assert #e1 : #e2; ... }\] (b))
\replacewith(#allmodal(b)) 
\heuristics(simplify_prog)
Choices: {assertions:off,programRules:Java}}
```

## skip assert

```
skipAssert_2 {
\find(#allmodal ( (modal operator))\[{ .. assert #e1; ... }\] (b))
\replacewith(#allmodal(b)) 
\heuristics(simplify_prog)
Choices: {assertions:off,programRules:Java}}
```

## sortsDisjoint1

```
sortsDisjoint1 {
\find(equals(x,y))
\varcond(\not\sub(Null, G), \disjointModuloNull(G, H), )
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## sortsDisjoint2

```
sortsDisjoint2 {
\find(equals(x,y))
\varcond(\not\sub(Null, H), \disjointModuloNull(G, H), )
\replacewith(false) 
\heuristics(concrete)
Choices: {}}
```

## sortsDisjointModuloNull

```
sortsDisjointModuloNull {
\find(equals(x,y))
\varcond(\strict\sub(Null, G), \strict\sub(Null, H), \disjointModuloNull(G, H), )
\replacewith(and(equals(x,null),equals(y,null))) 
\heuristics(simplify)
Choices: {}}
```

## special_constructor_call

```
special_constructor_call {
\find(#allmodal ( (modal operator))\[{ .. #scr ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. special-constructor-call(#scr) ... }\] (post)) 
\heuristics(method_expand)
Choices: {programRules:Java}}
```

## splitEquation

```
splitEquation {
\find(equals(splitEqLeft,splitEqRight))
\replacewith(and(geq(splitEqLeft,splitEqRight),leq(splitEqLeft,splitEqRight))) 

Choices: {}}
```

## splitEquationSucc

```
splitEquationSucc {
\find(==>equals(splitEqLeft,splitEqRight))
\replacewith([]==>[leq(splitEqLeft,splitEqRight)]) ;
\replacewith([]==>[geq(splitEqLeft,splitEqRight)]) 
\heuristics(notHumanReadable, inEqSimp_split_eq, inEqSimp_nonLin)
Choices: {}}
```

## split_or_strong

```
split_or_strong {
\find(or(b,c)==>)
\replacewith([c]==>[b]) ;
\replacewith([b]==>[]) 

Choices: {}}
```

## square_nonneg

```
square_nonneg {
\find(leq(Z(0(#)),mul(i0,i0)))
\replacewith(true) 

Choices: {}}
```

## startsWith

```
startsWith {
\find(clStartsWith(sourceStr,searchStr))
\replacewith(if-then-else(gt(seqLen(searchStr),seqLen(sourceStr)),false,equals(seqSub(sourceStr,Z(0(#)),seqLen(searchStr)),searchStr))) 
\heuristics(defOpsStartsEndsWith)
Choices: {Strings:on}}
```

## stringAssignment

```
stringAssignment {
\find(#normalassign ( (modal operator))\[{ .. #v=#slit; ... }\] (post))
\sameUpdateLevel\add [not(equals(strPool(#slit),null)),equals(boolean::select(heap,strPool(#slit),java.lang.Object::<created>),TRUE)]==>[] \replacewith(update-application(elem-update(#v (program Variable))(strPool(#slit)),#normalassign(post))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## stringConcat

```
stringConcat {
\find(#normalassign ( (modal operator))\[{ .. #v=#sstr1+#sstr2; ... }\] (post))
\sameUpdateLevel\add [equals(strContent(sk),seqConcat(strContent(#sstr1),strContent(#sstr2)))]==>[equals(sk,null)] \replacewith(update-application(elem-update(#v (program Variable))(sk),update-application(elem-update(heap)(create(heap,sk)),#normalassign(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## stringConcatBooleanLeft

```
stringConcatBooleanLeft {
\find(#normalassign ( (modal operator))\[{ .. #v=#seLeft+#sstrRight; ... }\] (post))
\sameUpdateLevel\add [equals(strContent(sk),seqConcat(if-then-else(equals(#seLeft,TRUE),seqConcat(seqSingleton(C(6(1(1(#))))),seqConcat(seqSingleton(C(4(1(1(#))))),seqConcat(seqSingleton(C(7(1(1(#))))),seqSingleton(C(1(0(1(#)))))))),seqConcat(seqSingleton(C(2(0(1(#))))),seqConcat(seqSingleton(C(7(9(#)))),seqConcat(seqSingleton(C(8(0(1(#))))),seqConcat(seqSingleton(C(5(1(1(#))))),seqSingleton(C(1(0(1(#)))))))))),strContent(#sstrRight)))]==>[equals(sk,null)] \replacewith(update-application(elem-update(#v (program Variable))(sk),update-application(elem-update(heap)(create(heap,sk)),#normalassign(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## stringConcatBooleanRight

```
stringConcatBooleanRight {
\find(#normalassign ( (modal operator))\[{ .. #v=#sstrLeft+#seRight; ... }\] (post))
\sameUpdateLevel\add [equals(strContent(sk),seqConcat(strContent(#sstrLeft),if-then-else(equals(#seRight,TRUE),seqConcat(seqSingleton(C(6(1(1(#))))),seqConcat(seqSingleton(C(4(1(1(#))))),seqConcat(seqSingleton(C(7(1(1(#))))),seqSingleton(C(1(0(1(#)))))))),seqConcat(seqSingleton(C(2(0(1(#))))),seqConcat(seqSingleton(C(7(9(#)))),seqConcat(seqSingleton(C(8(0(1(#))))),seqConcat(seqSingleton(C(5(1(1(#))))),seqSingleton(C(1(0(1(#))))))))))))]==>[equals(sk,null)] \replacewith(update-application(elem-update(#v (program Variable))(sk),update-application(elem-update(heap)(create(heap,sk)),#normalassign(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## stringConcatCharExpLeft

```
stringConcatCharExpLeft {
\find(#normalassign ( (modal operator))\[{ .. #v=#seLeft+#sstrRight; ... }\] (post))
\sameUpdateLevel\add [equals(strContent(sk),seqConcat(seqSingleton(#seLeft),strContent(#sstrRight)))]==>[equals(sk,null)] \replacewith(update-application(elem-update(#v (program Variable))(sk),update-application(elem-update(heap)(create(heap,sk)),#normalassign(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## stringConcatCharExpRight

```
stringConcatCharExpRight {
\find(#normalassign ( (modal operator))\[{ .. #v=#sstrLeft+#seRight; ... }\] (post))
\sameUpdateLevel\add [equals(strContent(sk),seqConcat(strContent(#sstrLeft),seqSingleton(#seRight)))]==>[equals(sk,null)] \replacewith(update-application(elem-update(#v (program Variable))(sk),update-application(elem-update(heap)(create(heap,sk)),#normalassign(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## stringConcatIntExpLeft

```
stringConcatIntExpLeft {
\find(#normalassign ( (modal operator))\[{ .. #v=#seLeft+#sstrRight; ... }\] (post))
\sameUpdateLevel\add [equals(strContent(sk),seqConcat(clTranslateInt(#seLeft),strContent(#sstrRight)))]==>[equals(sk,null)] \replacewith(update-application(elem-update(#v (program Variable))(sk),update-application(elem-update(heap)(create(heap,sk)),#normalassign(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## stringConcatIntExpRight

```
stringConcatIntExpRight {
\find(#normalassign ( (modal operator))\[{ .. #v=#sstrLeft+#seRight; ... }\] (post))
\sameUpdateLevel\add [equals(strContent(sk),seqConcat(strContent(#sstrLeft),clTranslateInt(#seRight)))]==>[equals(sk,null)] \replacewith(update-application(elem-update(#v (program Variable))(sk),update-application(elem-update(heap)(create(heap,sk)),#normalassign(post)))) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## stringConcatObjectLeft

```
stringConcatObjectLeft {
\find(#normalassign ( (modal operator))\[{ .. #v=#seLeft+#sstrRight; ... }\] (post))
\sameUpdateLevel\add [equals(#seLeft,null),equals(strContent(sk),seqConcat(strContent(null),strContent(#sstrRight)))]==>[equals(sk,null)] \replacewith(update-application(elem-update(#v (program Variable))(sk),update-application(elem-update(heap)(create(heap,sk)),#normalassign(post)))) ;
\add []==>[equals(#seLeft,null)] \replacewith(#normalassign ( (modal operator))\[{ .. #v=#seLeft.toString()+#sstrRight; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## stringConcatObjectRight

```
stringConcatObjectRight {
\find(#normalassign ( (modal operator))\[{ .. #v=#sstrLeft+#seRight; ... }\] (post))
\sameUpdateLevel\add [equals(#seRight,null),equals(strContent(sk),seqConcat(strContent(#sstrLeft),strContent(null)))]==>[equals(sk,null)] \replacewith(update-application(elem-update(#v (program Variable))(sk),update-application(elem-update(heap)(create(heap,sk)),#normalassign(post)))) ;
\add []==>[equals(#seRight,null)] \replacewith(#normalassign ( (modal operator))\[{ .. #v=#sstrLeft+#seRight.toString(); ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {}}
```

## sub

```
sub {
\find(sub(i,i0))
\replacewith(add(i,neg(i0))) 

Choices: {}}
```

## subSeqComplete

```
subSeqComplete {
\find(seqSub(seq,Z(0(#)),seqLen(seq)))
\replacewith(seq) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqCompleteSeqDef

```
subSeqCompleteSeqDef {
\find(seqSub(seqDef{i (variable)}(Z(0(#)),u,a),Z(0(#)),u))
\replacewith(seqDef{i (variable)}(Z(0(#)),u,a)) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqCompleteSeqDefEQ

```
subSeqCompleteSeqDefEQ {
\assumes ([equals(seqDef{i (variable)}(Z(0(#)),u,a),EQ)]==>[]) 
\find(seqSub(EQ,Z(0(#)),u))
\replacewith(seqDef{i (variable)}(Z(0(#)),u,a)) 
\heuristics(concrete, no_self_application)
Choices: {sequences:on}}
```

## subSeqConcat

```
subSeqConcat {
\find(seqSub(seqConcat(s1,s2),l,u))
\replacewith(seqConcat(seqSub(s1,l,if-then-else(lt(seqLen(s1),u),seqLen(s1),u)),seqSub(s2,if-then-else(lt(l,seqLen(s1)),Z(0(#)),sub(l,seqLen(s1))),sub(u,seqLen(s1))))) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## subSeqConcatEQ

```
subSeqConcatEQ {
\assumes ([equals(seqConcat(s1,s2),EQ)]==>[]) 
\find(seqSub(EQ,l,u))
\replacewith(seqConcat(seqSub(s1,l,if-then-else(lt(seqLen(s1),u),seqLen(s1),u)),seqSub(s2,if-then-else(lt(l,seqLen(s1)),Z(0(#)),sub(l,seqLen(s1))),sub(u,seqLen(s1))))) 
\heuristics(simplify_enlarging, no_self_application)
Choices: {sequences:on}}
```

## subSeqEmpty

```
subSeqEmpty {
\find(seqSub(seq,i,i))
\replacewith(seqEmpty) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqHeadSeqDef

```
subSeqHeadSeqDef {
\find(seqSub(seqConcat(seqDef{i (variable)}(Z(0(#)),u,a),seq),Z(0(#)),u))
\replacewith(seqDef{i (variable)}(Z(0(#)),u,a)) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqHeadSeqDefEQ

```
subSeqHeadSeqDefEQ {
\assumes ([equals(seqDef{i (variable)}(Z(0(#)),u,a),EQ)]==>[]) 
\find(seqSub(seqConcat(EQ,seq),Z(0(#)),u))
\replacewith(seqDef{i (variable)}(Z(0(#)),u,a)) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqSingleton

```
subSeqSingleton {
\find(seqSub(seqSingleton(x),Z(0(#)),Z(1(#))))
\replacewith(seqSingleton(x)) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqSingleton2

```
subSeqSingleton2 {
\find(seqSub(seqSingleton(x),l,u))
\replacewith(seqConcat(seqSub(seqEmpty,if-then-else(lt(l,Z(0(#))),l,Z(0(#))),if-then-else(lt(u,Z(0(#))),u,Z(0(#)))),seqConcat(if-then-else(and(leq(l,Z(0(#))),geq(u,Z(1(#)))),seqSingleton(x),seqEmpty),seqSub(seqEmpty,if-then-else(gt(l,Z(0(#))),l,Z(1(#))),if-then-else(gt(u,Z(0(#))),u,Z(1(#))))))) 
\heuristics(simplify_enlarging)
Choices: {sequences:on}}
```

## subSeqSingleton2EQ

```
subSeqSingleton2EQ {
\assumes ([equals(seqSingleton(x),EQ)]==>[]) 
\find(seqSub(EQ,l,u))
\replacewith(seqConcat(seqSub(seqEmpty,if-then-else(lt(l,Z(0(#))),l,Z(0(#))),if-then-else(lt(u,Z(0(#))),u,Z(0(#)))),seqConcat(if-then-else(and(leq(l,Z(0(#))),geq(u,Z(1(#)))),seqSingleton(x),seqEmpty),seqSub(seqEmpty,if-then-else(gt(l,Z(0(#))),l,Z(1(#))),if-then-else(gt(u,Z(0(#))),u,Z(1(#))))))) 
\heuristics(simplify_enlarging, no_self_application)
Choices: {sequences:on}}
```

## subSeqSingletonEQ

```
subSeqSingletonEQ {
\assumes ([equals(seqSingleton(x),EQ)]==>[]) 
\find(seqSub(EQ,Z(0(#)),Z(1(#))))
\replacewith(seqSingleton(x)) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqTail

```
subSeqTailR {
\find(seqSub(seqConcat(seqSingleton(x),seq),Z(1(#)),add(seqLen(seq),Z(1(#)))))
\replacewith(seq) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqTail

```
subSeqTailL {
\find(seqSub(seqConcat(seqSingleton(x),seq),Z(1(#)),add(Z(1(#)),seqLen(seq))))
\replacewith(seq) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqTail

```
subSeqTailEQR {
\assumes ([equals(seqLen(seq),EQ)]==>[]) 
\find(seqSub(seqConcat(seqSingleton(x),seq),Z(1(#)),add(EQ,Z(1(#)))))
\sameUpdateLevel\replacewith(seq) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## subSeqTail

```
subSeqTailEQL {
\assumes ([equals(seqLen(seq),EQ)]==>[]) 
\find(seqSub(seqConcat(seqSingleton(x),seq),Z(1(#)),add(Z(1(#)),EQ)))
\sameUpdateLevel\replacewith(seq) 
\heuristics(concrete)
Choices: {sequences:on}}
```

## sub_equations_left

```
sub_equations_left {
\assumes ([equals(i,i0)]==>[]) 
\find(equals(j,j0)==>)
\add [equals(sub(j,i),sub(j0,i0))]==>[] 

Choices: {}}
```

## sub_equations_right

```
sub_equations_right {
\assumes ([equals(i,i0)]==>[]) 
\find(==>equals(j,j0))
\add []==>[equals(sub(j,i),sub(j0,i0))] 

Choices: {}}
```

## sub_literals

```
sub_literals {
\find(sub(Z(iz),Z(jz)))
\replacewith(#sub(Z(iz),Z(jz))) 
\heuristics(simplify_literals)
Choices: {}}
```

## sub_sub_elim

```
sub_sub_elim {
\find(neg(neg(i)))
\replacewith(i) 

Choices: {}}
```

## sub_zero_1

```
sub_zero_1 {
\find(Z(neglit(0(#))))
\replacewith(Z(0(#))) 
\heuristics(simplify_literals)
Choices: {}}
```

## sub_zero_2

```
sub_zero_2 {
\find(sub(i,Z(0(#))))
\replacewith(i) 

Choices: {}}
```

## subsetOfAllLocs

```
subsetWithAllLocs {
\find(subset(s,allLocs))
\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfEmpty

```
subsetOfEmpty {
\find(subset(s,empty))
\replacewith(equals(s,empty)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfIntersectWithItSelf1

```
subsetOfIntersectWithItSelf1 {
\find(subset(s,intersect(s,s2)))
\replacewith(subset(s,s2)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfIntersectWithItSelf2

```
subsetOfIntersectWithItSelf2 {
\find(subset(s,intersect(s2,s)))
\replacewith(subset(s,s2)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfIntersectWithItSelfEQ1

```
subsetOfIntersectWithItSelfEQ1 {
\assumes ([equals(intersect(s,s2),EQ)]==>[]) 
\find(subset(s,EQ))
\sameUpdateLevel\replacewith(subset(s,s2)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfIntersectWithItSelfEQ2

```
subsetOfIntersectWithItSelfEQ2 {
\assumes ([equals(intersect(s2,s),EQ)]==>[]) 
\find(subset(s,EQ))
\sameUpdateLevel\replacewith(subset(s,s2)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfItself

```
subsetOfItself {
\find(subset(s,s))
\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfUnionWithItSelf1

```
subsetOfUnionWithItSelf1 {
\find(subset(s,union(s,s2)))
\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfUnionWithItSelf2

```
subsetOfUnionWithItSelf2 {
\find(subset(s,union(s2,s)))
\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfUnionWithItSelfEQ1

```
subsetOfUnionWithItSelfEQ1 {
\assumes ([equals(union(s,s2),EQ)]==>[]) 
\find(subset(s,EQ))
\sameUpdateLevel\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetOfUnionWithItSelfEQ2

```
subsetOfUnionWithItSelfEQ2 {
\assumes ([equals(union(s2,s),EQ)]==>[]) 
\find(subset(s,EQ))
\sameUpdateLevel\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetSingletonLeft

```
subsetSingletonLeft {
\find(subset(singleton(o,f),s))
\replacewith(elementOf(o,f,s)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## subsetSingletonLeftEQ

```
subsetSingletonLeftEQ {
\assumes ([equals(singleton(o,f),EQ)]==>[]) 
\find(subset(EQ,s))
\sameUpdateLevel\replacewith(elementOf(o,f,s)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## subsetSingletonRight

```
subsetSingletonRight {
\find(subset(s,singleton(o,f)))
\replacewith(or(equals(s,empty),equals(s,singleton(o,f)))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## subsetSingletonRightEQ

```
subsetSingletonRightEQ {
\assumes ([equals(singleton(o,f),EQ)]==>[]) 
\find(subset(s,EQ))
\sameUpdateLevel\replacewith(or(equals(s,empty),equals(s,singleton(o,f)))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## subsetToElementOf

```
subsetToElementOf {
\find(subset(s,s2))
\varcond(\notFreeIn(fv (variable), s2 (LocSet term)), \notFreeIn(fv (variable), s (LocSet term)), \notFreeIn(ov (variable), s2 (LocSet term)), \notFreeIn(ov (variable), s (LocSet term)))
\replacewith(all{ov (variable)}(all{fv (variable)}(imp(elementOf(ov,fv,s),elementOf(ov,fv,s2))))) 
\heuristics(semantics_blasting)
Choices: {programRules:Java}}
```

## subsetToElementOfRight

```
subsetToElementOfRight {
\find(==>subset(s,s2))
\varcond(\notFreeIn(fv (variable), s2 (LocSet term)), \notFreeIn(fv (variable), s (LocSet term)), \notFreeIn(ov (variable), s2 (LocSet term)), \notFreeIn(ov (variable), s (LocSet term)))
\replacewith([]==>[all{ov (variable)}(all{fv (variable)}(imp(elementOf(ov,fv,s),elementOf(ov,fv,s2))))]) 
\heuristics(setEqualityBlastingRight)
Choices: {programRules:Java}}
```

## subsetUnionLeft

```
subsetUnionLeft {
\find(subset(union(s,s2),s3))
\replacewith(and(subset(s,s3),subset(s2,s3))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## subsetUnionLeftEQ

```
subsetUnionLeftEQ {
\assumes ([equals(union(s,s2),EQ)]==>[]) 
\find(subset(EQ,s3))
\sameUpdateLevel\replacewith(and(subset(s,s3),subset(s2,s3))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## subsetWithAllLocs

```
subsetWithAllLocs2 {
\find(subset(allLocs,s))
\replacewith(equals(s,allLocs)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetWithEmpty

```
subsetWithEmpty {
\find(subset(empty,s))
\replacewith(true) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## subsetWithSetMinusLeft

```
subsetWithSetMinusLeft {
\find(subset(setMinus(s,s2),s3))
\replacewith(subset(s,union(s2,s3))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## subsetWithSetMinusLeftEQ

```
subsetWithSetMinusLeftEQ {
\assumes ([equals(setMinus(s,s2),EQ)]==>[]) 
\find(subset(EQ,s3))
\sameUpdateLevel\replacewith(subset(s,union(s2,s3))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## subst_to_eq

```
subst_to_eq {
\find(subst{u (variable)}(t,target))
\sameUpdateLevel\add [equals(sk,t)]==>[] \replacewith(subst{u (variable)}(sk,target)) 
\heuristics(simplify)
Choices: {}}
```

## subst_to_eq

```
subst_to_eq_for {
\find(subst{u (variable)}(t,phi))
\sameUpdateLevel\add [equals(sk,t)]==>[] \replacewith(subst{u (variable)}(sk,phi)) 
\heuristics(simplify)
Choices: {}}
```

## substringSubstring

```
substringSubstring {
\find(seqSub(seqSub(str,innerStartIdx,innerEndIdx),outerStartIdx,outerEndIdx))
\sameUpdateLevel\add [imp(and(and(and(and(and(geq(innerStartIdx,Z(0(#))),geq(innerEndIdx,innerStartIdx)),leq(innerEndIdx,seqLen(str))),geq(outerStartIdx,Z(0(#)))),geq(outerEndIdx,outerStartIdx)),leq(outerEndIdx,sub(innerEndIdx,innerStartIdx))),and(equals(seqSub(seqSub(str,innerStartIdx,innerEndIdx),outerStartIdx,outerEndIdx),newSym),equals(seqSub(str,add(outerStartIdx,innerStartIdx),add(innerStartIdx,outerEndIdx)),newSym)))]==>[] 
\heuristics(stringsIntroduceNewSym, stringsReduceSubstring)
Choices: {Strings:on}}
```

## substringSubstring2

```
substringSubstring2 {
\assumes ([equals(seqSub(str,innerStartIdx,innerEndIdx),innerSub)]==>[]) 
\find(seqSub(innerSub,outerStartIdx,outerEndIdx))
\sameUpdateLevel\add [imp(and(and(and(and(and(geq(innerStartIdx,Z(0(#))),geq(innerEndIdx,innerStartIdx)),leq(innerEndIdx,seqLen(str))),geq(outerStartIdx,Z(0(#)))),geq(outerEndIdx,outerStartIdx)),leq(outerEndIdx,sub(innerEndIdx,innerStartIdx))),and(equals(seqSub(innerSub,outerStartIdx,outerEndIdx),newSym),equals(seqSub(str,add(outerStartIdx,innerStartIdx),add(innerStartIdx,outerEndIdx)),newSym)))]==>[] 
\heuristics(stringsIntroduceNewSym, stringsReduceSubstring)
Choices: {Strings:on}}
```

## subtraction

```
compound_subtraction_1 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#nse-#se; ... }\] (post))
\varcond(\new(#v (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v = #nse;#lhs=#v-#se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## subtraction

```
compound_subtraction_2 {
\find(#allmodal ( (modal operator))\[{ .. #lhs=#e-#nse; ... }\] (post))
\varcond(\new(#v1 (program Variable), \typeof(#nse (program NonSimpleExpression))), \new(#v0 (program Variable), \typeof(#e (program Expression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#e) #v0 = #e;#typeof(#nse) #v1 = #nse;#lhs=#v0-#v1; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## subtraction

```
assignmentSubtractionInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt0-#seCharByteShortInt1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaSubInt(#seCharByteShortInt0,#seCharByteShortInt1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## subtraction

```
assignmentSubtractionLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seCharByteShortInt-#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaSubLong(#seCharByteShortInt,#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## subtraction

```
assignmentSubtractionLong2 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong-#seCharByteShortInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaSubLong(#seLong,#seCharByteShortInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## subtraction

```
assignmentSubtractionLong3 {
\find(#normalassign ( (modal operator))\[{ .. #loc=#seLong0-#seLong1; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaSubLong(#seLong0,#seLong1)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## subtraction

```
assignmentSubtractionBigint1 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seBigint-#seAny; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(sub(#seBigint,#seAny)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## subtraction

```
assignmentSubtractionBigint2 {
\find(#allmodal ( (modal operator))\[{ .. #loc=#seAny-#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(sub(#seAny,#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## sum_empty

```
sum_empty {
\find(sum{x (variable)}(FALSE,t))
\replacewith(Z(0(#))) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## sum_zero

```
sum_zero {
\find(sum{x (variable)}(range,Z(0(#))))
\replacewith(Z(0(#))) 
\heuristics(concrete)
Choices: {integerSimplificationRules:full}}
```

## superclasses_of_initialized_classes_are_initialized

```
superclasses_of_initialized_classes_are_initialized {
\assumes ([equals(boolean::select(heap,null,betaObj::<classInitialized>),TRUE),wellFormed(heap)]==>[]) 
\find(boolean::select(heap,null,alphaObj::<classInitialized>))
\sameUpdateLevel\varcond(\isReference[non_null]( betaObj ), \strict\sub(betaObj, alphaObj), )
\replacewith(TRUE) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## superclasses_of_initialized_classes_are_prepared

```
superclasses_of_initialized_classes_are_prepared {
\assumes ([equals(boolean::select(heap,null,betaObj::<classInitialized>),TRUE),wellFormed(heap)]==>[]) 
\find(boolean::select(heap,null,alphaObj::<classPrepared>))
\sameUpdateLevel\varcond(\sub(betaObj, alphaObj), )
\replacewith(TRUE) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## swapQuantifiersAll

```
swapQuantifiersAll {
\find(all{u (variable)}(all{v (variable)}(phi)))
\replacewith(all{v (variable)}(all{u (variable)}(phi))) 
\heuristics(swapQuantifiers)
Choices: {}}
```

## swapQuantifiersEx

```
swapQuantifiersEx {
\find(exists{u (variable)}(exists{v (variable)}(phi)))
\replacewith(exists{v (variable)}(exists{u (variable)}(phi))) 
\heuristics(swapQuantifiers)
Choices: {}}
```

## switch

```
switch {
\find(#allmodal ( (modal operator))\[{ .. #sw ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. switch-to-if(#sw) ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## switch_brackets

```
switch_brackets {
\find(add(add(i,i0),i1))
\replacewith(add(i,add(i0,i1))) 

Choices: {}}
```

## switch_params

```
switch_params {
\find(add(i0,i1))
\replacewith(add(i1,i0)) 

Choices: {}}
```

## synchronizedBlockEmpty

```
synchronizedBlockEmpty {
\find(==>#allmodal ( (modal operator))\[{ .. synchronized(#se) {} ... }\] (post))
\varcond(\isLocalVariable (#se (program SimpleExpression)), )
\replacewith([]==>[not(equals(#se,null))]) ;
\replacewith([]==>[#allmodal(post)]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ban,programRules:Java}}
```

## synchronizedBlockEmpty

```
synchronizedBlockEmpty {
\find(==>#allmodal ( (modal operator))\[{ .. synchronized(#se) {} ... }\] (post))
\varcond(\isLocalVariable (#se (program SimpleExpression)), )
\replacewith([]==>[#allmodal(post)]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:ignore,programRules:Java}}
```

## synchronizedBlockEmpty

```
synchronizedBlockEmpty {
\find(==>#allmodal ( (modal operator))\[{ .. synchronized(#se) {} ... }\] (post))
\varcond(\isLocalVariable (#se (program SimpleExpression)), )
\replacewith([]==>[#allmodal ( (modal operator))\[{ .. if (#se==null) {
    throw  new  java.lang.NullPointerException ();
  }
 ... }\] (post)]) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {runtimeExceptions:allow,programRules:Java}}
```

## synchronizedBlockEmpty

```
synchronizedBlockEmpty2 {
\find(#allmodal ( (modal operator))\[{ .. synchronized(#cr) {} ... }\] (post))
\replacewith(#allmodal(post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## synchronizedBlockEvalSync

```
synchronizedBlockEvalSync {
\find(#allmodal ( (modal operator))\[{ .. synchronized(#nsencr) {#slist} ... }\] (post))
\varcond(\new(#loc (program Variable), \typeof(#nsencr (program NonSimpleExpressionNoClassReference))), \isLocalVariable (#nsencr (program NonSimpleExpressionNoClassReference)), )
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nsencr) #loc = #nsencr;synchronized(#loc) {#slist} ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## theorem_of_archimedes

```
theorem_of_archimedes {
\assumes ([gt(i0,sub(i1,Z(1(#))))]==>[]) 
\find(lt(i0,i1)==>)
\replacewith([false]==>[]) 

Choices: {}}
```

## throwBox

```
throwBox {
\find(#box ( (modal operator))\[{throw #se;#slist}\] (post))
\replacewith(true) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## throwDiamond

```
throwDiamond {
\find(#diamond ( (modal operator))\[{throw #se;#slist}\] (post))
\replacewith(false) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## throwLabel

```
throwLabel {
\find(#allmodal ( (modal operator))\[{ .. #lb:throw #se; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. throw #se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## throwLabelBlock

```
throwLabelBlock {
\find(#allmodal ( (modal operator))\[{ .. #lb: {throw #se;#slist} ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. throw #se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## throwNull

```
throwNull {
\find(#allmodal ( (modal operator))\[{ .. throw null; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. throw new java.lang.NullPointerException (); ... }\] (post)) 

Choices: {programRules:Java}}
```

## throwUnfold

```
throwUnfold {
\find(#allmodal ( (modal operator))\[{ .. throw #nse; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#nse (program NonSimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#nse) #v0 = #nse;throw #v0; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## throwUnfold

```
throwUnfoldMore {
\find(#allmodal ( (modal operator))\[{ .. throw #se; ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#se (program SimpleExpression))), \isLocalVariable (#se (program SimpleExpression)), )
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#se) #v0 = #se;throw #v0; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## times_minus_one

```
times_minus_one_1 {
\find(mul(i,Z(neglit(1(#)))))
\replacewith(neg(i)) 

Choices: {}}
```

## times_minus_one

```
times_minus_one_2 {
\find(mul(Z(neglit(1(#))),i))
\replacewith(neg(i)) 

Choices: {}}
```

## times_one

```
times_one_1 {
\find(mul(i,Z(1(#))))
\replacewith(i) 

Choices: {}}
```

## times_one

```
times_one_2 {
\find(mul(Z(1(#)),i))
\replacewith(i) 

Choices: {}}
```

## times_zero

```
times_zero_1 {
\find(mul(i,Z(0(#))))
\replacewith(Z(0(#))) 
\heuristics(simplify_literals)
Choices: {}}
```

## times_zero

```
times_zero_2 {
\find(mul(Z(0(#)),i))
\replacewith(Z(0(#))) 
\heuristics(simplify_literals)
Choices: {}}
```

## transferPermission_empty

```
transferPermission_empty {
\find(transferPermission(split,from,to,depth,emptyPermission))
\replacewith(emptyPermission) 
\heuristics(concrete)
Choices: {permissions:on}}
```

## transferPermission_slice

```
transferPermission_slice {
\find(transferPermission(split,from,to,depth,slice(owners,p)))
\replacewith(if-then-else(equals(from,to),slice(owners,p),if-then-else(checkPermissionOwner(from,depth,owners),slice(insertPermissionOwner(from,to,depth,owners),if-then-else(equals(split,TRUE),slice(owners,p),transferPermission(split,from,to,depth,p))),slice(owners,transferPermission(split,from,to,depth,p))))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## translate#

```
translate# {
\find(clTranslateInt(Z(#)))
\replacewith(seqEmpty) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate0

```
translate0 {
\find(clTranslateInt(Z(0(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(8(4(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate1

```
translate1 {
\find(clTranslateInt(Z(1(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(9(4(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate2

```
translate2 {
\find(clTranslateInt(Z(2(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(0(5(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate3

```
translate3 {
\find(clTranslateInt(Z(3(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(1(5(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate4

```
translate4 {
\find(clTranslateInt(Z(4(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(2(5(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate5

```
translate5 {
\find(clTranslateInt(Z(5(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(3(5(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate6

```
translate6 {
\find(clTranslateInt(Z(6(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(4(5(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate7

```
translate7 {
\find(clTranslateInt(Z(7(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(5(5(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate8

```
translate8 {
\find(clTranslateInt(Z(8(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(6(5(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translate9

```
translate9 {
\find(clTranslateInt(Z(9(iz))))
\replacewith(seqConcat(clTranslateInt(Z(iz)),seqSingleton(C(7(5(#)))))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## translateJavaAdd

```
translateJavaAddInt {
\find(javaAddInt(left,right))
\replacewith(add(left,right)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaAdd

```
translateJavaAddLong {
\find(javaAddLong(left,right))
\replacewith(add(left,right)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaAdd

```
translateJavaAddInt {
\find(javaAddInt(left,right))
\replacewith(addJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaAdd

```
translateJavaAddLong {
\find(javaAddLong(left,right))
\replacewith(addJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaAdd

```
translateJavaAddInt {
\find(javaAddInt(left,right))
\replacewith(if-then-else(inInt(add(left,right)),add(left,right),javaAddIntOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaAdd

```
translateJavaAddLong {
\find(javaAddLong(left,right))
\replacewith(if-then-else(inLong(add(left,right)),add(left,right),javaAddLongOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaBitwiseAnd

```
translateJavaBitwiseAndInt {
\find(javaBitwiseAndInt(left,right))
\replacewith(andJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaBitwiseAnd

```
translateJavaBitwiseAndLong {
\find(javaBitwiseAndLong(left,right))
\replacewith(andJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaBitwiseOr

```
translateJavaBitwiseOrInt {
\find(javaBitwiseOrInt(left,right))
\replacewith(orJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaBitwiseOr

```
translateJavaBitwiseOrLong {
\find(javaBitwiseOrLong(left,right))
\replacewith(orJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaBitwiseOr

```
translateJavaBitwiseOrInt {
\find(javaBitwiseOrInt(left,right))
\replacewith(if-then-else(and(inInt(left),inInt(right)),orJint(left,right),javaBitwiseOrIntOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaBitwiseXOr

```
translateJavaBitwiseXOrInt {
\find(javaBitwiseXOrInt(left,right))
\replacewith(xorJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaBitwiseXOr

```
translateJavaBitwiseXOrLong {
\find(javaBitwiseXOrLong(left,right))
\replacewith(xorJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastByte {
\find(javaCastByte(left))
\replacewith(left) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastShort {
\find(javaCastShort(left))
\replacewith(left) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastInt {
\find(javaCastInt(left))
\replacewith(left) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastLong {
\find(javaCastLong(left))
\replacewith(left) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastChar {
\find(javaCastChar(left))
\replacewith(left) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastByte {
\find(javaCastByte(left))
\replacewith(moduloByte(left)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastShort {
\find(javaCastShort(left))
\replacewith(moduloShort(left)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastInt {
\find(javaCastInt(left))
\replacewith(moduloInt(left)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastLong {
\find(javaCastLong(left))
\replacewith(moduloLong(left)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastChar {
\find(javaCastChar(left))
\replacewith(moduloChar(left)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastByte {
\find(javaCastByte(left))
\replacewith(if-then-else(inByte(left),left,javaCastByteOverFlow(left))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastShort {
\find(javaCastShort(left))
\replacewith(if-then-else(inShort(left),left,javaCastShortOverFlow(left))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastInt {
\find(javaCastInt(left))
\replacewith(if-then-else(inInt(left),left,javaCastIntOverFlow(left))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastLong {
\find(javaCastLong(left))
\replacewith(if-then-else(inLong(left),left,javaCastLongOverFlow(left))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaCast

```
translateJavaCastChar {
\find(javaCastChar(left))
\replacewith(if-then-else(inChar(left),left,javaCastCharOverFlow(left))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaDiv

```
translateJavaDivInt {
\find(javaDivInt(left,right))
\replacewith(jdiv(left,right)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaDiv

```
translateJavaDivLong {
\find(javaDivLong(left,right))
\replacewith(jdiv(left,right)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaDiv

```
translateJavaDivInt {
\find(javaDivInt(left,right))
\replacewith(divJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaDiv

```
translateJavaDivLong {
\find(javaDivLong(left,right))
\replacewith(divJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaDiv

```
translateJavaDivInt {
\find(javaDivInt(left,right))
\replacewith(if-then-else(inInt(jdiv(left,right)),jdiv(left,right),javaDivIntOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaDiv

```
translateJavaDivLong {
\find(javaDivLong(left,right))
\replacewith(if-then-else(inLong(jdiv(left,right)),jdiv(left,right),javaDivLongOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaMod

```
translateJavaMod {
\find(javaMod(left,right))
\replacewith(jmod(left,right)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaMod

```
translateJavaMod {
\find(javaMod(left,right))
\replacewith(jmod(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaMod

```
translateJavaMod {
\find(javaMod(left,right))
\replacewith(jmod(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaMul

```
translateJavaMulInt {
\find(javaMulInt(left,right))
\replacewith(mul(left,right)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaMul

```
translateJavaMulLong {
\find(javaMulLong(left,right))
\replacewith(mul(left,right)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaMul

```
translateJavaMulInt {
\find(javaMulInt(left,right))
\replacewith(mulJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaMul

```
translateJavaMulLong {
\find(javaMulLong(left,right))
\replacewith(mulJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaMul

```
translateJavaMulInt {
\find(javaMulInt(left,right))
\replacewith(if-then-else(inInt(mul(left,right)),mul(left,right),javaMulIntOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaMul

```
translateJavaMulLong {
\find(javaMulLong(left,right))
\replacewith(if-then-else(inLong(mul(left,right)),mul(left,right),javaMulLongOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaShiftLeft

```
translateJavaShiftLeftInt {
\find(javaShiftLeftInt(left,right))
\replacewith(shiftleftJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaShiftLeft

```
translateJavaShiftLeftLong {
\find(javaShiftLeftLong(left,right))
\replacewith(shiftleftJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaShiftRight

```
translateJavaShiftRightInt {
\find(javaShiftRightInt(left,right))
\replacewith(shiftrightJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaShiftRight

```
translateJavaShiftRightLong {
\find(javaShiftRightLong(left,right))
\replacewith(shiftrightJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaSub

```
translateJavaSubInt {
\find(javaSubInt(left,right))
\replacewith(sub(left,right)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaSub

```
translateJavaSubLong {
\find(javaSubLong(left,right))
\replacewith(sub(left,right)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaSub

```
translateJavaSubInt {
\find(javaSubInt(left,right))
\replacewith(subJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaSub

```
translateJavaSubLong {
\find(javaSubLong(left,right))
\replacewith(subJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaSub

```
translateJavaSubInt {
\find(javaSubInt(left,right))
\replacewith(if-then-else(inInt(sub(left,right)),sub(left,right),javaSubIntOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaSub

```
translateJavaSubLong {
\find(javaSubLong(left,right))
\replacewith(if-then-else(inLong(sub(left,right)),sub(left,right),javaSubLongOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaUnaryMinus

```
translateJavaUnaryMinusInt {
\find(javaUnaryMinusInt(left))
\replacewith(neg(left)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaUnaryMinus

```
translateJavaUnaryMinusLong {
\find(javaUnaryMinusLong(left))
\replacewith(neg(left)) 
\heuristics(simplify, javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsIgnoringOF,programRules:Java}}
```

## translateJavaUnaryMinus

```
translateJavaUnaryMinusInt {
\find(javaUnaryMinusInt(left))
\replacewith(unaryMinusJint(left)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaUnaryMinus

```
translateJavaUnaryMinusLong {
\find(javaUnaryMinusLong(left))
\replacewith(unaryMinusJlong(left)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaUnaryMinus

```
translateJavaBitwiseNegation {
\find(javaBitwiseNegation(left))
\replacewith(sub(neg(left),Z(1(#)))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaUnaryMinus

```
translateJavaUnaryMinusInt {
\find(javaUnaryMinusInt(left))
\replacewith(if-then-else(inInt(neg(left)),neg(left),javaUnaryMinusIntOverFlow(left))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaUnaryMinus

```
translateJavaUnaryMinusLong {
\find(javaUnaryMinusLong(left))
\replacewith(if-then-else(inLong(neg(left)),neg(left),javaUnaryMinusLongOverFlow(left))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateJavaUnsignedShiftRight

```
translateJavaUnsignedShiftRightInt {
\find(javaUnsignedShiftRightInt(left,right))
\replacewith(unsignedshiftrightJint(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaUnsignedShiftRight

```
translateJavaUnsignedShiftRightLong {
\find(javaUnsignedShiftRightLong(left,right))
\replacewith(unsignedshiftrightJlong(left,right)) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:javaSemantics,programRules:Java}}
```

## translateJavaUnsignedShiftRight

```
translateJavaUnsignedShiftRightInt {
\find(javaUnsignedShiftRightInt(left,right))
\replacewith(if-then-else(inInt(unsignedshiftrightJint(left,right)),unsignedshiftrightJint(left,right),javaUnsignedShiftRightOverFlow(left,right))) 
\heuristics(javaIntegerSemantics)
Choices: {intRules:arithmeticSemanticsCheckingOF,programRules:Java}}
```

## translateNegLit

```
translateNegLit {
\find(clTranslateInt(Z(neglit(iz))))
\replacewith(seqConcat(seqSingleton(C(5(4(#)))),clTranslateInt(Z(iz)))) 
\heuristics(integerToString)
Choices: {Strings:on}}
```

## true_left

```
true_left {
\find(true==>)
\replacewith([]==>[]) 
\heuristics(concrete)
Choices: {}}
```

## true_to_not_false

```
true_to_not_false {
\find(equals(bo,TRUE))
\replacewith(not(equals(bo,FALSE))) 

Choices: {}}
```

## tryBreak

```
tryBreakLabel {
\find(#allmodal ( (modal operator))\[{ .. try {break ;
    #slist
  }
  #cs
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. break ;
 ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryBreak

```
tryFinallyBreakLabel {
\find(#allmodal ( (modal operator))\[{ .. try {break ;
    #slist
  }
  #cs               finally {
    #slist2
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {#slist2}break ;
 ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryBreak

```
tryBreak {
\find(#allmodal ( (modal operator))\[{ .. try {break ;
    #slist
  }
  #cs
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. break ;
 ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryBreak

```
tryFinallyBreak {
\find(#allmodal ( (modal operator))\[{ .. try {break ;
    #slist
  }
  #cs               finally {
    #slist2
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {#slist2}break ;
 ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryCatchFinallyThrow

```
tryCatchFinallyThrow {
\find(#allmodal ( (modal operator))\[{ .. try {throw #se;#slist} catch (#t #v0) {
    #slist1
  }
  #cs               finally {
    #slist2
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. if (#se==null) {
                           try {
      throw  new  java.lang.NullPointerException ();
    }                            catch (#t #v0) {
      #slist1
    }
    #cs                           finally {
      #slist2
    }
  }
  else 
    if (#se instanceof #t) {
                           try {
        #t #v0;
        #v0=(#t)#se;
        #slist1
      }                           finally {
        #slist2
      }
    }
    else  {
                           try {
        throw  #se;
      }
      #cs                           finally {
        #slist2
      }
    }
 ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryCatchThrow

```
tryCatchThrow {
\find(#allmodal ( (modal operator))\[{ .. try {throw #se;#slist} catch (#t #v0) {
    #slist1
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. if (#se==null) {
                            try {
      throw  new  java.lang.NullPointerException ();
    }                             catch (#t #v0) {
      #slist1
    }
  }
  else 
    if (#se instanceof #t) {
      #t #v0;
      #v0=(#t)#se;
      #slist1
    }
    else  {
                            throw  #se;
    }
 ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryCatchThrow

```
tryMultipleCatchThrow {
\find(#allmodal ( (modal operator))\[{ .. try {throw #se;#slist} catch (#t #v0) {
    #slist1
  }                catch (#t2 #v1) {
    #slist3
  }
  #cs
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. if (#se==null) {
                           try {
      throw  new  java.lang.NullPointerException ();
    }                            catch (#t #v0) {
      #slist1
    }                            catch (#t2 #v1) {
      #slist3
    }
    #cs
  }
  else 
    if (#se instanceof #t) {
      #t #v0;
      #v0=(#t)#se;
      #slist1
    }
    else  {
                           try {
        throw  #se;
      }                            catch (#t2 #v1) {
        #slist3
      }
      #cs
    }
 ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryEmpty

```
tryEmpty {
\find(#allmodal ( (modal operator))\[{ .. try {}#cs ... }\] (post))
\replacewith(#allmodal(post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryEmpty

```
tryFinallyEmpty {
\find(#allmodal ( (modal operator))\[{ .. try {}#csfinally {
    #slist2
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {#slist2} ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryFinallyThrow

```
tryFinallyThrow {
\find(#allmodal ( (modal operator))\[{ .. try {throw #se;#slist}finally {
    #slist2
  }
 ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#se (program SimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. if (#se==null) { {
      #slist2
    }
                           throw  new  java.lang.NullPointerException ();
  }  else  {#typeof    (#se) #v0 = #se; {
      #slist2
    }
                           throw  #v0;
  }
 ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryReturn

```
tryReturn {
\find(#allmodal ( (modal operator))\[{ .. try {return #se;#slist}#cs ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. return #se; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryReturn

```
tryFinallyReturn {
\find(#allmodal ( (modal operator))\[{ .. try {return #se;#slist}#csfinally {
    #slist2
  }
 ... }\] (post))
\varcond(\new(#v0 (program Variable), \typeof(#se (program SimpleExpression))))
\replacewith(#allmodal ( (modal operator))\[{ .. #typeof(#se) #v0 = #se; {#slist2}return #v0; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryReturn

```
tryReturnNoValue {
\find(#allmodal ( (modal operator))\[{ .. try {return ;#slist}#cs ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. return ; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## tryReturn

```
tryFinallyReturnNoValue {
\find(#allmodal ( (modal operator))\[{ .. try {return ;#slist}#csfinally {
    #slist2
  }
 ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ ..  {#slist2}return ; ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## twoPermissions

```
twoPermissions {
\find(twoPermissions(o1,o2,p))
\varcond(\notFreeIn(ol2 (variable), o2 (java.lang.Object term)), \notFreeIn(ol1 (variable), o2 (java.lang.Object term)), \notFreeIn(ol2 (variable), o1 (java.lang.Object term)), \notFreeIn(ol1 (variable), o1 (java.lang.Object term)), \notFreeIn(ol2 (variable), p (Permission term)), \notFreeIn(ol1 (variable), p (Permission term)))
\replacewith(exists{ol1 (variable), ol2 (variable)}(equals(p,slice(consPermissionOwnerList(o1,ol1),slice(consPermissionOwnerList(o2,ol2),emptyPermission))))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## typeEq

```
typeEq {
\find(equals(s,t1)==>)
\add [equals(H::instance(s),TRUE),equals(G::instance(t1),TRUE)]==>[] 

Choices: {}}
```

## typeEq

```
typeEqDerived {
\assumes ([equals(s,t1)]==>[]) 
\find(H::instance(s))
\sameUpdateLevel\replacewith(TRUE) 
\heuristics(concrete, simplify)
Choices: {}}
```

## typeEq

```
typeEqDerived2 {
\assumes ([equals(s,t1)]==>[]) 
\find(G::instance(t1))
\sameUpdateLevel\replacewith(TRUE) 
\heuristics(concrete, simplify)
Choices: {}}
```

## typeStatic

```
typeStatic {
\find(s)
\sameUpdateLevel\add [equals(G::instance(s),TRUE)]==>[] 

Choices: {}}
```

## unaryMinus

```
unaryMinusInt {
\find(#normalassign ( (modal operator))\[{ .. #loc=-#seCharByteShortInt; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaUnaryMinusInt(#seCharByteShortInt)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## unaryMinus

```
unaryMinusBigint {
\find(#allmodal ( (modal operator))\[{ .. #loc=-#seBigint; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(neg(#seBigint)),#allmodal(post))) 
\heuristics(executeIntegerAssignment)
Choices: {bigint:on,programRules:Java}}
```

## unary_minus

```
unaryMinusLong {
\find(#normalassign ( (modal operator))\[{ .. #loc=-#seLong; ... }\] (post))
\replacewith(update-application(elem-update(#loc (program Variable))(javaUnaryMinusLong(#seLong)),#normalassign(post))) 
\heuristics(executeIntegerAssignment)
Choices: {programRules:Java}}
```

## unionEqualsEmpty

```
unionEqualsEmpty {
\find(equals(union(s,s2),empty))
\replacewith(and(equals(s,empty),equals(s2,empty))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## unionEqualsEmptyEQ

```
unionEqualsEmptyEQ {
\assumes ([equals(union(s,s2),EQ)]==>[]) 
\find(equals(EQ,empty))
\sameUpdateLevel\replacewith(and(equals(s,empty),equals(s2,empty))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## unionIntersectItself

```
unionIntersectItself {
\find(union(intersect(s1,s2),s1))
\replacewith(s1) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## unionIntersectItself_2

```
unionIntersectItself_2 {
\find(union(intersect(s2,s1),s1))
\replacewith(s1) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## unionIntersectItself_3

```
unionIntersectItself_3 {
\find(union(union(s,intersect(s1,s2)),s1))
\replacewith(union(s,s1)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## unionIntersectItself_4

```
unionIntersectItself_4 {
\find(union(union(s,intersect(s2,s1)),s1))
\replacewith(union(s,s1)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## unionIntersectItself_5

```
unionIntersectItself_5 {
\find(union(union(intersect(s1,s2),s),s1))
\replacewith(union(s,s1)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## unionIntersectItself_6

```
unionIntersectItself_6 {
\find(union(union(intersect(s2,s1),s),s1))
\replacewith(union(s,s1)) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## unionWithAllLocs

```
unionWithAllLocs {
\find(union(allLocs,s))
\replacewith(allLocs) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## unionWithAllLocs

```
unionWithAllLocsRight {
\find(union(s,allLocs))
\replacewith(allLocs) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## unionWithEmpty

```
unionWithEmpty {
\find(union(empty,s))
\replacewith(s) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## unionWithEmpty

```
unionWithEmptyRight {
\find(union(s,empty))
\replacewith(s) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## unionWithItself

```
unionWithItself {
\find(union(s,s))
\replacewith(s) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## unionWithSingletonEqualsUnionWithSingleton

```
unionWithSingletonEqualsUnionWithSingleton {
\find(equals(union(s1,singleton(o,f)),union(s2,singleton(o,f))))
\replacewith(equals(setMinus(s1,singleton(o,f)),setMinus(s2,singleton(o,f)))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## unionWithSingletonEqualsUnionWithSingleton_2

```
unionWithSingletonEqualsUnionWithSingleton_2 {
\find(equals(union(singleton(o,f),s1),union(singleton(o,f),s2)))
\replacewith(equals(setMinus(s1,singleton(o,f)),setMinus(s2,singleton(o,f)))) 
\heuristics(simplify)
Choices: {programRules:Java}}
```

## unsignedShiftRightJintDef

```
unsignedShiftRightJintDef {
\find(unsignedshiftrightJint(left,right))
\replacewith(if-then-else(geq(left,Z(0(#))),shiftrightJint(left,right),addJint(shiftrightJint(left,right),shiftleftJint(Z(2(#)),sub(Z(1(3(#))),mod(right,Z(2(3(#))))))))) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## unusedLabel

```
unusedLabel {
\find(#allmodal ( (modal operator))\[{ .. #lb:#s ... }\] (post))
\varcond(\not\freeLabelIn (#lb,#s), )
\replacewith(#allmodal ( (modal operator))\[{ .. #s ... }\] (post)) 
\heuristics(simplify_prog)
Choices: {programRules:Java}}
```

## variableDeclaration

```
variableDeclaration {
\find(#allmodal ( (modal operator))\[{ .. #t #v0; ... }\] (post))
\addprogvars {#v0 (program Variable)} \replacewith(#allmodal(post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## variableDeclaration

```
variableDeclarationFinal {
\find(#allmodal ( (modal operator))\[{ .. final#t #v0; ... }\] (post))
\addprogvars {#v0 (program Variable)} \replacewith(#allmodal(post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## variableDeclaration

```
variableDeclarationAssign {
\find(#allmodal ( (modal operator))\[{ .. #t #v0 = #vi; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. #t #v0;#v0=#vi; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## variableDeclaration

```
variableDeclarationFinalAssign {
\find(#allmodal ( (modal operator))\[{ .. final#t #v0 = #vi; ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. final#t #v0;#v0=#vi; ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## variableDeclaration

```
variableDeclarationMult {
\find(#allmodal ( (modal operator))\[{ .. #multvardecl ... }\] (post))
\replacewith(#allmodal ( (modal operator))\[{ .. multiple-var-decl(#multvardecl) ... }\] (post)) 
\heuristics(simplify_prog_subset, simplify_prog)
Choices: {programRules:Java}}
```

## wd_Constant_Formula

```
wd_Constant_Formula {
\find(WD(f<<l (termLabel)>>))
\varcond(\isConstant (f (formula)), \not\hasLabel (l (termLabel), undef), )
\replacewith(true) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Constant_Term

```
wd_Constant_Term {
\find(wd(t<<l (termLabel)>>))
\varcond(\isConstant (t (any term)), \not\hasLabel (l (termLabel), undef), )
\replacewith(true) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Equality_Pred

```
wd_Equality_Pred {
\find(WD(equals(s,t)))
\replacewith(and(wd(s),wd(t))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_F_Logical_Op_And

```
wd_F_Logical_Op_And {
\find(F(and(a,b)))
\replacewith(or(F(a),F(b))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Logical_Op_Cond_Form

```
wd_F_Logical_Op_Cond_Form {
\find(F(if-then-else(a,b,c)))
\replacewith(or(or(and(T(a),F(b)),and(F(a),F(c))),and(F(b),F(c)))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Logical_Op_Eqv

```
wd_F_Logical_Op_Eqv {
\find(F(equiv(a,b)))
\replacewith(or(and(T(a),F(b)),and(F(a),T(b)))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Logical_Op_ExCond_Form

```
wd_F_Logical_Op_ExCond_Form {
\find(F(ifExThenElse{j (variable)}(a,b,c)))
\varcond(\notFreeIn(j (variable), jPrime (int skolem term)), \notFreeIn(j (variable), c (formula)))
\replacewith(or(or(exists{j (variable)}(and(and(T(a),F(b)),imp(and(wellOrderLeqInt(jPrime,j),not(equals(jPrime,j))),subst{j (variable)}(jPrime,F(a))))),all{j (variable)}(and(F(a),F(c)))),all{j (variable)}(and(F(b),F(c))))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Logical_Op_Imp

```
wd_F_Logical_Op_Imp {
\find(F(imp(a,b)))
\replacewith(and(T(a),F(b))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Logical_Op_Neg

```
wd_F_Logical_Op_Neg {
\find(F(not(a)))
\replacewith(T(a)) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Logical_Op_Or

```
wd_F_Logical_Op_Or {
\find(F(or(a,b)))
\replacewith(and(F(a),F(b))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Logical_Quant_All

```
wd_F_Logical_Quant_All {
\find(F(all{i (variable)}(a)))
\replacewith(exists{i (variable)}(F(a))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Logical_Quant_Exist

```
wd_F_Logical_Quant_Exist {
\find(F(exists{i (variable)}(a)))
\replacewith(all{i (variable)}(F(a))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Resolve

```
wd_F_Resolve {
\find(F(a))
\varcond(\not\hasSubFormulas (a (formula)), )
\replacewith(and(WD(a),not(a))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_F_Subst_Formula

```
wd_F_Subst_Formula {
\find(F(subst{v (variable)}(u,f)))
\replacewith(and(wd(u),subst{v (variable)}(u,F(f)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Anon

```
wd_Heap_Anon {
\find(wd(anon(h,l,g)))
\replacewith(and(and(and(and(wd(h),wd(l)),wd(g)),wellFormed(h)),wellFormed(g))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_ArrLength

```
wd_Heap_ArrLength {
\find(wd(length(o)))
\varcond( \not \isArray(o (java.lang.Object term)), )
\replacewith(and(wd(o),not(equals(o,null)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Create

```
wd_Heap_Create {
\find(wd(create(h,o)))
\replacewith(and(and(and(wd(h),wd(o)),wellFormed(h)),not(equals(o,null)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Memset

```
wd_Heap_Memset {
\find(wd(memset(h,l,a)))
\replacewith(and(and(and(wd(h),wd(l)),wd(a)),wellFormed(h))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Pred_ArrStoreValid

```
wd_Heap_Pred_ArrStoreValid {
\find(WD(arrayStoreValid(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Pred_NonNull

```
wd_Heap_Pred_NonNull {
\find(WD(nonNull(h,o,i)))
\replacewith(and(and(wd(h),wd(o)),wd(i))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Pred_WellFormed

```
wd_Heap_Pred_WellFormed {
\find(WD(wellFormed(h)))
\replacewith(wd(h)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Reference

```
wd_Heap_Reference {
\find(wd(alpha::select(h,o,f)))
\varcond(\isArray(o (java.lang.Object term)), \not\isStaticField(f (Field term)), )
\replacewith(and(and(and(and(and(wd(h),wd(o)),wd(f)),wellFormed(h)),not(equals(o,null))),or(equals(f,java.lang.Object::<created>),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Reference_Array

```
wd_Heap_Reference_Array {
\find(wd(alpha::select(h,o,arr(i))))
\varcond( \not \isArray(o (java.lang.Object term)), )
\replacewith(and(and(and(and(and(and(and(wd(h),wd(o)),wd(i)),wellFormed(h)),not(equals(o,null))),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE)),leq(Z(0(#)),i)),lt(i,length(o)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Reference_Created

```
wd_Heap_Reference_Created {
\find(wd(alpha::select(h,o,java.lang.Object::<created>)))
\replacewith(and(and(and(wd(h),wd(o)),wellFormed(h)),not(equals(o,null)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Reference_Static

```
wd_Heap_Reference_Static {
\find(wd(alpha::select(h,o,f)))
\varcond(\isArray(o (java.lang.Object term)), \isStaticField(f (Field term)), )
\replacewith(and(and(and(and(wd(h),wd(o)),wd(f)),wellFormed(h)),equals(o,null))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Heap_Store

```
wd_Heap_Store {
\find(wd(store(h,o,f,a)))
\replacewith(and(and(and(and(and(and(wd(h),wd(o)),wd(f)),wd(a)),wellFormed(h)),not(equals(o,null))),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_AllElemsArr

```
wd_LocSet_AllElemsArr {
\find(wd(allElementsOfArray(h,o,l)))
\varcond( \not \isArray(o (java.lang.Object term)), )
\replacewith(and(and(and(and(and(wd(h),wd(o)),wd(l)),wellFormed(h)),not(equals(o,null))),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_AllElemsArrLocsets

```
wd_LocSet_AllElemsArrLocsets {
\find(wd(allElementsOfArrayLocsets(h,o,l)))
\varcond( \not \isArray(o (java.lang.Object term)), )
\replacewith(and(and(and(and(and(wd(h),wd(o)),wd(l)),wellFormed(h)),not(equals(o,null))),equals(boolean::select(h,o,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_AllFields

```
wd_LocSet_AllFields {
\find(wd(allFields(o)))
\varcond(\isArray(o (java.lang.Object term)), )
\replacewith(wd(o)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_AllFieldsArr

```
wd_LocSet_AllFieldsArr {
\find(wd(allFields(o)))
\varcond( \not \isArray(o (java.lang.Object term)), )
\replacewith(and(wd(o),not(equals(o,null)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_AllObjects

```
wd_LocSet_AllObjects {
\find(wd(allObjects(f)))
\replacewith(wd(f)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_ArrRange

```
wd_LocSet_ArrRange {
\find(wd(arrayRange(o,i,j)))
\varcond( \not \isArray(o (java.lang.Object term)), )
\replacewith(and(and(and(and(and(and(wd(o),wd(i)),wd(j)),not(equals(o,null))),leq(Z(0(#)),i)),leq(i,j)),lt(j,length(o)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Diff

```
wd_LocSet_Diff {
\find(wd(setMinus(l,s)))
\replacewith(and(wd(l),wd(s))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_FreshLocs

```
wd_LocSet_FreshLocs {
\find(wd(freshLocs(h)))
\replacewith(and(wd(h),wellFormed(h))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_InfiniteUnion

```
wd_LocSet_InfiniteUnion {
\find(wd(infiniteUnion{a (variable)}(l)))
\replacewith(all{a (variable)}(wd(l))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_InfiniteUnion

```
wd_LocSet_InfiniteUnion2 {
\find(wd(infiniteUnion{a (variable), b (variable)}(l)))
\replacewith(all{a (variable)}(all{b (variable)}(wd(l)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Intersect

```
wd_LocSet_Intersect {
\find(wd(intersect(l,s)))
\replacewith(and(wd(l),wd(s))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Pred_Disjoint

```
wd_LocSet_Pred_Disjoint {
\find(WD(disjoint(l,s)))
\replacewith(and(wd(l),wd(s))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Pred_ElementOf

```
wd_LocSet_Pred_ElementOf {
\find(WD(elementOf(o,f,l)))
\varcond(\not\isStaticField(f (Field term)), )
\replacewith(and(and(and(wd(o),wd(f)),wd(l)),not(equals(o,null)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Pred_ElementOf_Static

```
wd_LocSet_Pred_ElementOf_Static {
\find(WD(elementOf(o,f,l)))
\varcond(\isStaticField(f (Field term)), )
\replacewith(and(and(and(wd(o),wd(f)),wd(l)),equals(o,null))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Pred_InHeap

```
wd_LocSet_Pred_InHeap {
\find(WD(createdInHeap(l,h)))
\replacewith(and(and(wd(l),wd(h)),wellFormed(h))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Pred_Subset

```
wd_LocSet_Pred_Subset {
\find(WD(subset(l,s)))
\replacewith(and(wd(l),wd(s))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Singleton

```
wd_LocSet_Singleton {
\find(wd(singleton(o,f)))
\varcond(\isArray(o (java.lang.Object term)), \not\isStaticField(f (Field term)), )
\replacewith(and(and(wd(o),wd(f)),not(equals(o,null)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Singleton_Arr

```
wd_LocSet_Singleton_Arr {
\find(wd(singleton(o,arr(i))))
\varcond( \not \isArray(o (java.lang.Object term)), )
\replacewith(and(and(and(and(wd(o),wd(i)),not(equals(o,null))),leq(Z(0(#)),i)),lt(i,length(o)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Singleton_Quant

```
wd_LocSet_Singleton_Quant {
\find(all{c (variable)}(wd(singleton(o,f))))
\varcond(\isArray(o (java.lang.Object term)), )
\replacewith(all{c (variable)}(and(wd(o),wd(f)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Singleton_Static

```
wd_LocSet_Singleton_Static {
\find(wd(singleton(o,f)))
\varcond(\isArray(o (java.lang.Object term)), \isStaticField(f (Field term)), )
\replacewith(and(and(wd(o),wd(f)),equals(o,null))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_LocSet_Union

```
wd_LocSet_Union {
\find(wd(union(l,s)))
\replacewith(and(wd(l),wd(s))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Logical_Op_And

```
wd_Logical_Op_And {
\find(WD(and(a,b)<<l (termLabel)>>))
\varcond(\not\hasLabel (l (termLabel), SC), )
\replacewith(and(WD(a),WD(b))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_And

```
wd_Logical_Op_And {
\find(WD(and(a,b)))
\replacewith(or(or(and(WD(a),not(a)),and(WD(b),not(b))),and(WD(a),WD(b)))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Op_AndSC

```
wd_Logical_Op_AndSC {
\find(WD(and(a,b)<<l (termLabel)>>))
\varcond(\hasLabel (l (termLabel), SC), )
\replacewith(and(WD(a),imp(a,WD(b)))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_Cond_Expr

```
wd_Logical_Op_Cond_Expr {
\find(wd(if-then-else(a,s,t)))
\replacewith(and(and(WD(a),imp(a,wd(s))),imp(not(a),wd(t)))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_Cond_Expr

```
wd_Logical_Op_Cond_Expr {
\find(wd(if-then-else(a,s,t)))
\replacewith(or(or(and(and(WD(a),wd(s)),a),and(and(WD(a),wd(t)),not(a))),and(and(wd(s),wd(t)),equals(s,t)))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Op_Cond_Expr

```
wd_Logical_Op_Cond_Expr {
\find(wd(if-then-else(a,s,t)))
\replacewith(or(or(and(T(a),wd(s)),and(F(a),wd(t))),and(and(wd(s),wd(t)),equals(s,t)))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_Logical_Op_Cond_Form

```
wd_Logical_Op_Cond_Form {
\find(WD(if-then-else(a,b,c)))
\replacewith(and(and(WD(a),imp(a,WD(b))),imp(not(a),WD(c)))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_Cond_Form

```
wd_Logical_Op_Cond_Form {
\find(WD(if-then-else(a,b,c)))
\replacewith(or(or(and(and(WD(a),WD(b)),a),and(and(WD(a),WD(c)),not(a))),and(and(WD(b),WD(c)),equiv(b,c)))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Op_Eqv

```
wd_Logical_Op_Eqv {
\find(WD(equiv(a,b)))
\replacewith(and(WD(a),WD(b))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_Eqv

```
wd_Logical_Op_Eqv {
\find(WD(equiv(a,b)))
\replacewith(and(WD(a),WD(b))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Op_ExCond_Expr

```
wd_Logical_Op_ExCond_Expr {
\find(wd(ifExThenElse{j (variable)}(a,s,t)))
\varcond(\notFreeIn(j (variable), t (any term)))
\replacewith(and(and(all{j (variable)}(WD(a)),imp(all{j (variable)}(not(a)),wd(t))),all{j (variable)}(imp(a,wd(s))))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_ExCond_Expr

```
wd_Logical_Op_ExCond_Expr {
\find(wd(ifExThenElse{j (variable)}(a,s,t)))
\varcond(\notFreeIn(j (variable), jPrime (int skolem term)), \notFreeIn(j (variable), t (any term)))
\replacewith(or(or(exists{j (variable)}(and(and(and(WD(a),wd(s)),a),imp(and(wellOrderLeqInt(jPrime,j),not(equals(jPrime,j))),subst{j (variable)}(jPrime,and(WD(a),not(a)))))),all{j (variable)}(and(and(WD(a),wd(t)),not(a)))),all{j (variable)}(and(and(wd(s),wd(t)),equals(s,t))))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Op_ExCond_Expr

```
wd_Logical_Op_ExCond_Expr {
\find(wd(ifExThenElse{j (variable)}(a,s,t)))
\varcond(\notFreeIn(j (variable), jPrime (int skolem term)), \notFreeIn(j (variable), t (any term)))
\replacewith(or(or(exists{j (variable)}(and(and(T(a),wd(s)),imp(and(wellOrderLeqInt(jPrime,j),not(equals(jPrime,j))),subst{j (variable)}(jPrime,F(a))))),all{j (variable)}(and(F(a),wd(t)))),all{j (variable)}(and(and(wd(s),wd(t)),equals(s,t))))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_Logical_Op_ExCond_Form

```
wd_Logical_Op_ExCond_Form {
\find(WD(ifExThenElse{j (variable)}(a,b,c)))
\varcond(\notFreeIn(j (variable), c (formula)))
\replacewith(and(and(all{j (variable)}(WD(a)),imp(all{j (variable)}(not(a)),WD(c))),all{j (variable)}(imp(a,WD(b))))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_ExCond_Form

```
wd_Logical_Op_ExCond_Form {
\find(WD(ifExThenElse{j (variable)}(a,b,c)))
\varcond(\notFreeIn(j (variable), jPrime (int skolem term)), \notFreeIn(j (variable), c (formula)))
\replacewith(or(or(exists{j (variable)}(and(and(and(WD(a),WD(b)),a),imp(and(wellOrderLeqInt(jPrime,j),not(equals(jPrime,j))),subst{j (variable)}(jPrime,and(WD(a),not(a)))))),all{j (variable)}(and(and(WD(a),WD(c)),not(a)))),all{j (variable)}(and(and(WD(b),WD(c)),equiv(b,c))))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Op_Imp

```
wd_Logical_Op_Imp {
\find(WD(imp(a,b)))
\replacewith(and(WD(a),imp(a,WD(b)))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_Imp

```
wd_Logical_Op_Imp {
\find(WD(imp(a,b)))
\replacewith(or(or(and(WD(a),not(a)),and(WD(b),b)),and(WD(a),WD(b)))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Op_Neg

```
wd_Logical_Op_Neg {
\find(WD(not(a)))
\replacewith(WD(a)) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_Neg

```
wd_Logical_Op_Neg {
\find(WD(not(a)))
\replacewith(WD(a)) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Op_Or

```
wd_Logical_Op_Or {
\find(WD(or(a,b)<<l (termLabel)>>))
\varcond(\not\hasLabel (l (termLabel), SC), )
\replacewith(and(WD(a),WD(b))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Op_Or

```
wd_Logical_Op_Or {
\find(WD(or(a,b)))
\replacewith(or(or(and(WD(a),a),and(WD(b),b)),and(WD(a),WD(b)))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Op_OrSC

```
wd_Logical_Op_OrSC {
\find(WD(or(a,b)<<l (termLabel)>>))
\varcond(\hasLabel (l (termLabel), SC), )
\replacewith(and(WD(a),imp(not(a),WD(b)))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Quant_All

```
wd_Logical_Quant_All {
\find(WD(all{i (variable)}(a)))
\replacewith(all{i (variable)}(WD(a))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Quant_All

```
wd_Logical_Quant_All {
\find(WD(all{i (variable)}(a)))
\replacewith(or(exists{i (variable)}(and(WD(a),not(a))),all{i (variable)}(WD(a)))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Logical_Quant_Exist

```
wd_Logical_Quant_Exist {
\find(WD(exists{i (variable)}(a)))
\replacewith(all{i (variable)}(WD(a))) 
\heuristics(simplify)
Choices: {wdOperator:L,wdChecks:on}}
```

## wd_Logical_Quant_Exist

```
wd_Logical_Quant_Exist {
\find(WD(exists{i (variable)}(a)))
\replacewith(or(exists{i (variable)}(and(WD(a),a)),all{i (variable)}(WD(a)))) 
\heuristics(simplify)
Choices: {wdOperator:D,wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_Byte {
\find(wd(javaCastByte(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_Short {
\find(wd(javaCastShort(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_Int {
\find(wd(javaCastInt(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_Long {
\find(wd(javaCastLong(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_Char {
\find(wd(javaCastChar(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_ByteOverFlow {
\find(wd(javaCastByteOverFlow(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_ShortOverFlow {
\find(wd(javaCastShortOverFlow(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_IntOverFlow {
\find(wd(javaCastIntOverFlow(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_LongOverFlow {
\find(wd(javaCastLongOverFlow(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Cast

```
wd_Numerical_Cast_CharOverFlow {
\find(wd(javaCastCharOverFlow(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Const

```
wd_Numerical_Const {
\find(wd(n))
\replacewith(true) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Const

```
wd_Numerical_Const_Z {
\find(wd(Z(n)))
\replacewith(true) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Const

```
wd_Numerical_Const_C {
\find(wd(C(n)))
\replacewith(true) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Mod

```
wd_Numerical_Mod_Byte {
\find(wd(moduloByte(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Mod

```
wd_Numerical_Mod_Short {
\find(wd(moduloShort(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Mod

```
wd_Numerical_Mod_Int {
\find(wd(moduloInt(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Mod

```
wd_Numerical_Mod_Long {
\find(wd(moduloLong(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Mod

```
wd_Numerical_Mod_Char {
\find(wd(moduloChar(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Add

```
wd_Numerical_Op_Add {
\find(wd(add(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Add

```
wd_Numerical_Op_AddJInt {
\find(wd(addJint(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Add

```
wd_Numerical_Op_AddJLong {
\find(wd(addJlong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Add

```
wd_Numerical_Op_AddInt {
\find(wd(javaAddInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Add

```
wd_Numerical_Op_AddLong {
\find(wd(javaAddLong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Add

```
wd_Numerical_Op_AddIntOverFlow {
\find(wd(javaAddIntOverFlow(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Add

```
wd_Numerical_Op_AddLongOverFlow {
\find(wd(javaAddLongOverFlow(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_And

```
wd_Numerical_Op_AndJInt {
\find(wd(andJint(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_And

```
wd_Numerical_Op_AndJLong {
\find(wd(andJlong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_And

```
wd_Numerical_Op_BitAndInt {
\find(wd(javaBitwiseAndInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_And

```
wd_Numerical_Op_BitAndLong {
\find(wd(javaBitwiseAndLong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Div

```
wd_Numerical_Op_Div {
\find(wd(div(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Div

```
wd_Numerical_Op_JDiv {
\find(wd(jdiv(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Div

```
wd_Numerical_Op_DivJInt {
\find(wd(divJint(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Div

```
wd_Numerical_Op_DivJLong {
\find(wd(divJlong(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Div

```
wd_Numerical_Op_DivInt {
\find(wd(javaDivInt(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Div

```
wd_Numerical_Op_DivLong {
\find(wd(javaDivLong(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Div

```
wd_Numerical_Op_DivIntOverFlow {
\find(wd(javaDivIntOverFlow(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Div

```
wd_Numerical_Op_DivLongOverFlow {
\find(wd(javaDivLongOverFlow(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Minus

```
wd_Numerical_Op_MinusJInt {
\find(wd(unaryMinusJint(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Minus

```
wd_Numerical_Op_MinusJLong {
\find(wd(unaryMinusJlong(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Minus

```
wd_Numerical_Op_MinusInt {
\find(wd(javaUnaryMinusInt(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Minus

```
wd_Numerical_Op_MinusLong {
\find(wd(javaUnaryMinusLong(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Minus

```
wd_Numerical_Op_MinusIntOverFlow {
\find(wd(javaUnaryMinusIntOverFlow(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Minus

```
wd_Numerical_Op_MinusLongOverFlow {
\find(wd(javaUnaryMinusLongOverFlow(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mod

```
wd_Numerical_Op_Mod {
\find(wd(mod(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mod

```
wd_Numerical_Op_JMod {
\find(wd(jmod(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mod

```
wd_Numerical_Op_ModJInt {
\find(wd(modJint(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mod

```
wd_Numerical_Op_ModJLong {
\find(wd(modJlong(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mod

```
wd_Numerical_Op_JavaMod {
\find(wd(javaMod(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mod

```
wd_Numerical_Op_JavaModOverFlow {
\find(wd(javaModOverFlow(a,b)))
\replacewith(and(and(wd(a),wd(b)),not(equals(b,Z(0(#)))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mul

```
wd_Numerical_Op_Mul {
\find(wd(mul(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mul

```
wd_Numerical_Op_MulJInt {
\find(wd(mulJint(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mul

```
wd_Numerical_Op_MulJLong {
\find(wd(mulJlong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mul

```
wd_Numerical_Op_MulInt {
\find(wd(javaMulInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mul

```
wd_Numerical_Op_MulLong {
\find(wd(javaMulLong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mul

```
wd_Numerical_Op_MulIntOverFlow {
\find(wd(javaMulIntOverFlow(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Mul

```
wd_Numerical_Op_MulLongOverFlow {
\find(wd(javaMulLongOverFlow(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Neg

```
wd_Numerical_Op_Neg {
\find(wd(neg(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Neg

```
wd_Numerical_Op_BitNeg {
\find(wd(javaBitwiseNegation(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Or

```
wd_Numerical_Op_OrJInt {
\find(wd(orJint(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Or

```
wd_Numerical_Op_OrJLong {
\find(wd(orJlong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Or

```
wd_Numerical_Op_BitOrInt {
\find(wd(javaBitwiseOrInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Or

```
wd_Numerical_Op_BitOrLong {
\find(wd(javaBitwiseOrLong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftLeft

```
wd_Numerical_Op_ShiftLeftInt {
\find(wd(shiftleftJint(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftLeft

```
wd_Numerical_Op_ShiftLeftLong {
\find(wd(shiftleftJlong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftLeft

```
wd_Numerical_Op_JavaShiftLeftInt {
\find(wd(javaShiftLeftInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftLeft

```
wd_Numerical_Op_JavaShiftLeftLong {
\find(wd(javaShiftLeftLong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftRight

```
wd_Numerical_Op_ShiftRightInt {
\find(wd(shiftrightJint(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftRight

```
wd_Numerical_Op_ShiftRightLong {
\find(wd(shiftrightJlong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftRight

```
wd_Numerical_Op_UShiftRightInt {
\find(wd(unsignedshiftrightJint(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftRight

```
wd_Numerical_Op_UShiftRightLong {
\find(wd(unsignedshiftrightJlong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftRight

```
wd_Numerical_Op_JavaShiftRightInt {
\find(wd(javaShiftRightInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftRight

```
wd_Numerical_Op_JavaShiftRightLong {
\find(wd(javaShiftRightLong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftRight

```
wd_Numerical_Op_JavaUnsignedShiftRightInt {
\find(wd(javaUnsignedShiftRightInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_ShiftRight

```
wd_Numerical_Op_JavaUnsignedShiftRightLong {
\find(wd(javaUnsignedShiftRightLong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Sub

```
wd_Numerical_Op_Sub {
\find(wd(sub(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Sub

```
wd_Numerical_Op_SubJInt {
\find(wd(subJint(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Sub

```
wd_Numerical_Op_SubJLong {
\find(wd(subJlong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Sub

```
wd_Numerical_Op_SubInt {
\find(wd(javaSubInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Sub

```
wd_Numerical_Op_SubLong {
\find(wd(javaSubLong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Sub

```
wd_Numerical_Op_SubIntOverFlow {
\find(wd(javaSubIntOverFlow(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Sub

```
wd_Numerical_Op_SubLongOverFlow {
\find(wd(javaSubLongOverFlow(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Xor

```
wd_Numerical_Op_XorJInt {
\find(wd(xorJint(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Xor

```
wd_Numerical_Op_XorJLong {
\find(wd(xorJlong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Xor

```
wd_Numerical_Op_BitXOrInt {
\find(wd(javaBitwiseXOrInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Op_Xor

```
wd_Numerical_Op_BitXOrLong {
\find(wd(javaBitwiseXOrLong(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_Geq

```
wd_Numerical_Pred_Geq {
\find(WD(geq(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_Gt

```
wd_Numerical_Pred_Gt {
\find(WD(gt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_InByte

```
wd_Numerical_Pred_InByte {
\find(WD(inByte(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_InChar

```
wd_Numerical_Pred_InChar {
\find(WD(inChar(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_InInt

```
wd_Numerical_Pred_InInt {
\find(WD(inInt(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_InLong

```
wd_Numerical_Pred_InLong {
\find(WD(inLong(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_InShort

```
wd_Numerical_Pred_InShort {
\find(WD(inShort(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_Leq

```
wd_Numerical_Pred_Leq {
\find(WD(leq(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_Lt

```
wd_Numerical_Pred_Lt {
\find(WD(lt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Pred_WellOrdered

```
wd_Numerical_Pred_WellOrdered {
\find(WD(wellOrderLeqInt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Quant_Bprod

```
wd_Numerical_Quant_Bprod {
\find(wd(bprod{i (variable)}(a,b,c)))
\varcond(\notFreeIn(i (variable), b (int term)), \notFreeIn(i (variable), a (int term)))
\replacewith(and(and(wd(a),wd(b)),all{i (variable)}(imp(and(leq(a,i),lt(i,b)),wd(c))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Quant_Bsum

```
wd_Numerical_Quant_Bsum {
\find(wd(bsum{i (variable)}(a,b,c)))
\varcond(\notFreeIn(i (variable), b (int term)), \notFreeIn(i (variable), a (int term)))
\replacewith(and(and(wd(a),wd(b)),all{i (variable)}(imp(and(leq(a,i),lt(i,b)),wd(c))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Quant_Max

```
wd_Numerical_Quant_Max {
\find(wd(max{j (variable)}(f,c)))
\replacewith(and(all{j (variable)}(wd(f)),all{j (variable)}(imp(equals(f,TRUE),wd(c))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Quant_Min

```
wd_Numerical_Quant_Min {
\find(wd(min{j (variable)}(f,c)))
\replacewith(and(all{j (variable)}(wd(f)),all{j (variable)}(imp(equals(f,TRUE),wd(c))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Quant_Prod

```
wd_Numerical_Quant_Prod {
\find(wd(prod{j (variable)}(f,c)))
\replacewith(and(all{j (variable)}(wd(f)),all{j (variable)}(imp(equals(f,TRUE),wd(c))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Numerical_Quant_Sum

```
wd_Numerical_Quant_Sum {
\find(wd(sum{j (variable)}(f,c)))
\replacewith(and(all{j (variable)}(wd(f)),all{j (variable)}(imp(equals(f,TRUE),wd(c))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Pair

```
wd_Pair {
\find(wd(pair(s,t)))
\replacewith(and(wd(s),wd(t))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Reach_Pred_Acc

```
wd_Reach_Pred_Acc {
\find(WD(acc(h,l,o,p)))
\replacewith(and(and(and(and(wd(h),wd(l)),wd(o)),wd(p)),wellFormed(h))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Reach_Pred_Reach

```
wd_Reach_Pred_Reach {
\find(WD(reach(h,l,o,p,i)))
\replacewith(and(and(and(and(and(wd(h),wd(l)),wd(o)),wd(p)),wd(i)),wellFormed(h))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_RegEx

```
wd_RegEx {
\find(wd(regEx(s)))
\replacewith(wd(s)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_RegEx_Alt

```
wd_RegEx_Alt {
\find(wd(alt(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_RegEx_Concat

```
wd_RegEx_Concat {
\find(wd(regExConcat(a,b)))
\replacewith(and(wd(a),wd(b))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_RegEx_Opt

```
wd_RegEx_Opt {
\find(wd(opt(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_RegEx_Plus

```
wd_RegEx_Plus {
\find(wd(repeatPlus(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_RegEx_Pred_Match

```
wd_RegEx_Pred_Match {
\find(WD(match(a,s)))
\replacewith(and(wd(a),wd(s))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_RegEx_Repeat

```
wd_RegEx_Repeat {
\find(wd(repeat(a,n)))
\replacewith(and(wd(a),leq(Z(0(#)),n))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_RegEx_Star

```
wd_RegEx_Star {
\find(wd(repeatStar(a)))
\replacewith(wd(a)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Concat

```
wd_Seq_Concat {
\find(wd(seqConcat(s,q)))
\replacewith(and(wd(s),wd(q))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Def

```
wd_Seq_Def {
\find(wd(seqDef{i (variable)}(m,n,t)))
\varcond(\notFreeIn(i (variable), n (int term)), \notFreeIn(i (variable), m (int term)))
\replacewith(and(and(and(wd(m),wd(n)),leq(m,n)),all{i (variable)}(imp(and(leq(m,i),lt(i,n)),wd(t))))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Get

```
wd_Seq_Get {
\find(wd(alpha::seqGet(s,n)))
\replacewith(and(and(and(wd(s),wd(n)),leq(Z(0(#)),n)),lt(n,seqLen(s)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_IndexOf

```
wd_Seq_IndexOf {
\find(wd(seqIndexOf(s,t)))
\replacewith(and(wd(s),wd(t))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Length

```
wd_Seq_Length {
\find(wd(seqLen(s)))
\replacewith(wd(s)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_NPermInv

```
wd_Seq_NPermInv {
\find(wd(seqNPermInv(s)))
\replacewith(and(wd(s),seqNPerm(s))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Pred_NPerm

```
wd_Seq_Pred_NPerm {
\find(WD(seqNPerm(s)))
\replacewith(wd(s)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Pred_Perm

```
wd_Seq_Pred_Perm {
\find(WD(seqPerm(s,q)))
\replacewith(and(wd(s),wd(q))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Remove

```
wd_Seq_Remove {
\find(wd(seqRemove(s,n)))
\replacewith(and(and(and(wd(s),wd(n)),leq(Z(0(#)),n)),lt(n,seqLen(s)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Reverse

```
wd_Seq_Reverse {
\find(wd(seqReverse(s)))
\replacewith(wd(s)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Singleton

```
wd_Seq_Singleton {
\find(wd(seqSingleton(t)))
\replacewith(wd(t)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Sub

```
wd_Seq_Sub {
\find(wd(seqSub(s,m,n)))
\replacewith(and(and(and(and(and(wd(s),wd(m)),wd(n)),leq(Z(0(#)),m)),leq(m,n)),leq(n,seqLen(s)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Seq_Swap

```
wd_Seq_Swap {
\find(wd(seqSwap(s,m,n)))
\replacewith(and(and(and(and(and(and(wd(s),wd(m)),wd(n)),leq(Z(0(#)),m)),leq(Z(0(#)),n)),lt(m,seqLen(s))),lt(n,seqLen(s)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_Hash

```
wd_String_Hash {
\find(wd(clHashCode(c)))
\replacewith(wd(c)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_IndexOfChar

```
wd_String_IndexOfChar {
\find(wd(clIndexOfChar(c,i,j)))
\replacewith(and(and(and(and(and(wd(i),wd(j)),wd(c)),leq(Z(0(#)),i)),leq(i,j)),lt(j,seqLen(c)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_IndexOfStr

```
wd_String_IndexOfStr {
\find(wd(clIndexOfCl(l,i,c)))
\replacewith(and(and(and(and(wd(c),wd(i)),wd(l)),leq(Z(0(#)),i)),lt(add(i,seqLen(c)),seqLen(l)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_LastIndexOfChar

```
wd_String_LastIndexOfChar {
\find(wd(clLastIndexOfChar(c,i,j)))
\replacewith(and(and(and(and(and(wd(i),wd(j)),wd(c)),leq(Z(0(#)),i)),leq(i,j)),lt(j,seqLen(c)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_LastIndexOfStr

```
wd_String_LastIndexOfStr {
\find(wd(clLastIndexOfCl(l,i,c)))
\replacewith(and(and(and(and(wd(c),wd(i)),wd(l)),leq(Z(0(#)),i)),lt(add(i,seqLen(c)),seqLen(l)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_Pred_Contains

```
wd_String_Pred_Contains {
\find(WD(clContains(l,c)))
\replacewith(and(wd(c),wd(l))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_Pred_EndsWith

```
wd_String_Pred_EndsWith {
\find(WD(clEndsWith(l,c)))
\replacewith(and(wd(c),wd(l))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_Pred_StartsWith

```
wd_String_Pred_StartsWith {
\find(WD(clStartsWith(l,c)))
\replacewith(and(wd(c),wd(l))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_Replace

```
wd_String_Replace {
\find(wd(clReplace(c,i,j)))
\replacewith(and(and(and(and(and(and(wd(i),wd(j)),wd(c)),leq(Z(0(#)),i)),leq(Z(0(#)),j)),lt(i,seqLen(c))),lt(j,seqLen(c)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_RmvZeros

```
wd_String_RmvZeros {
\find(wd(clRemoveZeros(c)))
\replacewith(wd(c)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_String_Translate

```
wd_String_Translate {
\find(wd(clTranslateInt(i)))
\replacewith(wd(i)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Subst_Formula

```
wd_Subst_Formula {
\find(WD(subst{v (variable)}(u,f)))
\replacewith(and(wd(u),subst{v (variable)}(u,WD(f)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Subst_Term

```
wd_Subst_Term {
\find(wd(subst{v (variable)}(u,t)))
\replacewith(and(wd(u),subst{v (variable)}(u,wd(t)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_T_Logical_Op_And

```
wd_T_Logical_Op_And {
\find(T(and(a,b)))
\replacewith(and(T(a),T(b))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Logical_Op_Cond_Form

```
wd_T_Logical_Op_Cond_Form {
\find(T(if-then-else(a,b,c)))
\replacewith(or(or(and(T(a),T(b)),and(F(a),T(c))),and(T(b),T(c)))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Logical_Op_Eqv

```
wd_T_Logical_Op_Eqv {
\find(T(equiv(a,b)))
\replacewith(or(and(T(a),T(b)),and(F(a),F(b)))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Logical_Op_ExCond_Form

```
wd_T_Logical_Op_ExCond_Form {
\find(T(ifExThenElse{j (variable)}(a,b,c)))
\varcond(\notFreeIn(j (variable), jPrime (int skolem term)), \notFreeIn(j (variable), c (formula)))
\replacewith(or(or(exists{j (variable)}(and(and(T(a),T(b)),imp(and(wellOrderLeqInt(jPrime,j),not(equals(jPrime,j))),subst{j (variable)}(jPrime,F(a))))),all{j (variable)}(and(F(a),T(c)))),all{j (variable)}(and(T(b),T(c))))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Logical_Op_Imp

```
wd_T_Logical_Op_Imp {
\find(T(imp(a,b)))
\replacewith(or(F(a),T(b))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Logical_Op_Neg

```
wd_T_Logical_Op_Neg {
\find(T(not(a)))
\replacewith(F(a)) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Logical_Op_Or

```
wd_T_Logical_Op_Or {
\find(T(or(a,b)))
\replacewith(or(T(a),T(b))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Logical_Quant_All

```
wd_T_Logical_Quant_All {
\find(T(all{i (variable)}(a)))
\replacewith(all{i (variable)}(T(a))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Logical_Quant_Exist

```
wd_T_Logical_Quant_Exist {
\find(T(exists{i (variable)}(a)))
\replacewith(exists{i (variable)}(T(a))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Resolve

```
wd_T_Resolve {
\find(T(a))
\varcond(\not\hasSubFormulas (a (formula)), )
\replacewith(and(WD(a),a)) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wd_T_Subst_Formula

```
wd_T_Subst_Formula {
\find(T(subst{v (variable)}(u,f)))
\replacewith(and(wd(u),subst{v (variable)}(u,T(f)))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Type_Cast

```
wd_Type_Cast {
\find(wd(alpha::cast(t)))
\replacewith(and(wd(t),equals(alpha::instance(t),TRUE))) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Type_ExactInstance

```
wd_Type_ExactInstance {
\find(wd(alpha::exactInstance(t)))
\replacewith(wd(t)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Type_Instance

```
wd_Type_Instance {
\find(wd(alpha::instance(t)))
\replacewith(wd(t)) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Undef_Formula

```
wd_Undef_Formula {
\find(WD(f<<l (termLabel)>>))
\varcond(\isConstant (f (formula)), \hasLabel (l (termLabel), undef), )
\replacewith(false) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Undef_Term

```
wd_Undef_Term {
\find(wd(t<<l (termLabel)>>))
\varcond(\isConstant (t (any term)), \hasLabel (l (termLabel), undef), )
\replacewith(false) 
\heuristics(simplify)
Choices: {wdChecks:on}}
```

## wd_Y_Split

```
wd_Y_Split {
\find(WD(a))
\varcond(\hasSubFormulas (a (formula)), )
\replacewith(or(T(a),F(a))) 
\heuristics(simplify)
Choices: {wdOperator:Y,wdChecks:on}}
```

## wellFormedAnon

```
wellFormedAnon {
\find(wellFormed(anon(h,s,h2)))
\succedentPolarity\replacewith(and(wellFormed(h),wellFormed(h2))) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## wellFormedAnonEQ

```
wellFormedAnonEQ {
\assumes ([equals(anon(h,s,h2),EQ)]==>[]) 
\find(wellFormed(EQ))
\sameUpdateLevel\succedentPolarity\replacewith(and(wellFormed(h),wellFormed(h2))) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## wellFormedCreate

```
wellFormedCreate {
\find(wellFormed(create(h,o)))
\succedentPolarity\replacewith(wellFormed(h)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## wellFormedMemsetArrayObject

```
wellFormedMemsetArrayObject {
\find(wellFormed(memset(h,arrayRange(ar,lo,up),x)))
\succedentPolarity\varcond(\hasSort(\elemSort(ar (java.lang.Object term)), alpha), )
\replacewith(or(and(wellFormed(h),equals(x,null)),and(equals(boolean::select(h,x,java.lang.Object::<created>),TRUE),arrayStoreValid(ar,x)))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## wellFormedMemsetArrayPrimitive

```
wellFormedMemsetArrayPrimitive {
\find(wellFormed(memset(h,arrayRange(ar,lo,up),x)))
\succedentPolarity\varcond(\hasSort(\elemSort(ar (java.lang.Object term)), alpha), \not\sub(beta, java.lang.Object), \not\sub(beta, LocSet), \sub(beta, alpha), )
\replacewith(wellFormed(h)) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## wellFormedMemsetLocSetEQ

```
wellFormedMemsetLocSetEQ {
\assumes ([equals(memset(h,s,x),EQ)]==>[]) 
\find(wellFormed(EQ))
\sameUpdateLevel\succedentPolarity\replacewith(and(wellFormed(h),createdInHeap(x,h))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## wellFormedMemsetObjectEQ

```
wellFormedMemsetObjectEQ {
\assumes ([equals(memset(h,s,x),EQ)]==>[]) 
\find(wellFormed(EQ))
\sameUpdateLevel\succedentPolarity\replacewith(or(and(wellFormed(h),equals(x,null)),equals(boolean::select(h,x,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## wellFormedMemsetPrimitiveEQ

```
wellFormedMemsetPrimitiveEQ {
\assumes ([equals(memset(h,s,x),EQ)]==>[]) 
\find(wellFormed(EQ))
\sameUpdateLevel\succedentPolarity\varcond(\not\sub(beta, java.lang.Object), \not\sub(beta, LocSet), )
\replacewith(wellFormed(h)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## wellFormedStoreArray

```
wellFormedStoreArray {
\find(wellFormed(store(h,o,arr(idx),x)))
\succedentPolarity\varcond(\hasSort(\elemSort(o (java.lang.Object term)), alpha), )
\replacewith(or(and(wellFormed(h),equals(x,null)),and(equals(boolean::select(h,x,java.lang.Object::<created>),TRUE),arrayStoreValid(o,x)))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## wellFormedStoreLocSet

```
wellFormedStoreLocSet {
\find(wellFormed(store(h,o,f,x)))
\succedentPolarity\varcond(\fieldType(f (Field term), alpha), \sub(LocSet, alpha), )
\replacewith(and(wellFormed(h),createdInHeap(x,h))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## wellFormedStoreLocSetEQ

```
wellFormedStoreLocSetEQ {
\assumes ([equals(store(h,o,f,x),EQ)]==>[]) 
\find(wellFormed(EQ))
\sameUpdateLevel\succedentPolarity\replacewith(and(wellFormed(h),createdInHeap(x,h))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## wellFormedStoreObject

```
wellFormedStoreObject {
\find(wellFormed(store(h,o,f,x)))
\succedentPolarity\varcond(\fieldType(f (Field term), alpha), )
\replacewith(or(and(wellFormed(h),equals(x,null)),and(equals(boolean::select(h,x,java.lang.Object::<created>),TRUE),equals(alpha::instance(x),TRUE)))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## wellFormedStoreObjectEQ

```
wellFormedStoreObjectEQ {
\assumes ([equals(store(h,o,f,x),EQ)]==>[]) 
\find(wellFormed(EQ))
\sameUpdateLevel\succedentPolarity\replacewith(or(and(wellFormed(h),equals(x,null)),equals(boolean::select(h,x,java.lang.Object::<created>),TRUE))) 
\heuristics(simplify_enlarging)
Choices: {programRules:Java}}
```

## wellFormedStorePrimitive

```
wellFormedStorePrimitive {
\find(wellFormed(store(h,o,f,x)))
\succedentPolarity\varcond(\fieldType(f (Field term), alpha), \not\sub(beta, java.lang.Object), \not\sub(beta, LocSet), \sub(beta, alpha), )
\replacewith(wellFormed(h)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## wellFormedStorePrimitiveArray

```
wellFormedStorePrimitiveArray {
\find(wellFormed(store(h,o,arr(idx),x)))
\succedentPolarity\varcond(\hasSort(\elemSort(o (java.lang.Object term)), alpha), \not\sub(beta, java.lang.Object), \not\sub(beta, LocSet), \sub(beta, alpha), )
\replacewith(wellFormed(h)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## wellFormedStorePrimitiveEQ

```
wellFormedStorePrimitiveEQ {
\assumes ([equals(store(h,o,f,x),EQ)]==>[]) 
\find(wellFormed(EQ))
\sameUpdateLevel\succedentPolarity\varcond(\not\sub(beta, java.lang.Object), \not\sub(beta, LocSet), )
\replacewith(wellFormed(h)) 
\heuristics(concrete)
Choices: {programRules:Java}}
```

## writePermission

```
writePermission {
\find(writePermission(p))
\replacewith(writePermissionObject(currentThread,p)) 
\heuristics(simplify_enlarging)
Choices: {}}
```

## writePermissionAfterFullTransfer

```
writePermissionAfterFullTransfer {
\assumes ([writePermissionObject(o1,p)]==>[]) 
\find(writePermissionObject(o2,transferPermission(FALSE,o1,o2,Z(0(#)),p)))
\replacewith(true) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## writePermissionAfterFullTransferEQ

```
writePermissionAfterFullTransferEQ {
\assumes ([writePermissionObject(o1,p2),equals(transferPermission(FALSE,o1,o2,Z(0(#)),p2),p1)]==>[]) 
\find(writePermissionObject(o2,p1))
\replacewith(true) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## writePermissionAfterReturn

```
writePermissionAfterReturn {
\assumes ([writePermissionObject(o1,p)]==>[]) 
\find(writePermissionObject(o2,returnPermission(o1,o2,p)))
\replacewith(true) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## writePermissionAfterReturnEQ

```
writePermissionAfterReturnEQ {
\assumes ([writePermissionObject(o1,p2),equals(returnPermission(o1,o2,p2),p1)]==>[]) 
\find(writePermissionObject(o2,p1))
\replacewith(true) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## writePermissionEmpty

```
writePermissionEmpty {
\find(writePermissionObject(o,emptyPermission))
\replacewith(true) 
\heuristics(concrete)
Choices: {permissions:on}}
```

## writePermissionImpliesReadPermission

```
writePermissionImpliesReadPermission {
\assumes ([writePermissionObject(o,p)]==>[]) 
\find(readPermissionObject(o,p))
\replacewith(true) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## writePermissionObject

```
writePermissionObject {
\find(writePermissionObject(o,p))
\replacewith(true) 
\heuristics(concrete)
Choices: {permissions:off}}
```

## writePermissionOtherNoPermissionCurrentRead

```
writePermissionOtherNoPermissionCurrentRead {
\assumes ([writePermissionObject(o1,p)]==>[equals(o2,o1)]) 
\find(readPermissionObject(o2,p))
\replacewith(false) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## writePermissionOtherNoPermissionCurrentWrite

```
writePermissionOtherNoPermissionCurrentWrite {
\assumes ([writePermissionObject(o1,p)]==>[equals(o2,o1)]) 
\find(writePermissionObject(o2,p))
\replacewith(false) 
\heuristics(simplify)
Choices: {permissions:on}}
```

## writePermissionSlice

```
writePermissionSlice {
\find(writePermissionObject(o,slice(ol,p)))
\replacewith(and(checkPermissionOwner(o,Z(0(#)),ol),writePermissionObject(o,p))) 
\heuristics(simplify_enlarging)
Choices: {permissions:on}}
```

## xorJIntDef

```
xorJIntDef {
\find(xorJint(left,right))
\replacewith(moduloInt(binaryXOr(left,right))) 
\heuristics(simplify)
Choices: {}}
```

## zadd_left_cancel0

```
zadd_left_cancel0 {
\find(equals(i0,add(i0,i1)))
\replacewith(equals(i1,Z(0(#)))) 

Choices: {}}
```

## zero_leq_mult_iff

```
zero_leq_mult_iff {
\find(leq(Z(0(#)),mul(i0,i1)))
\replacewith(or(and(leq(Z(0(#)),i0),leq(Z(0(#)),i1)),and(leq(i0,Z(0(#))),leq(i1,Z(0(#)))))) 

Choices: {}}
```

## zero_less_mult_iff

```
zero_less_mult_iff {
\find(lt(Z(0(#)),mul(i0,i1)))
\replacewith(or(and(lt(Z(0(#)),i0),lt(Z(0(#)),i1)),and(lt(i0,Z(0(#))),lt(i1,Z(0(#)))))) 

Choices: {}}
```