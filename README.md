# CRF-Model
Following features are currenly captured.

1. Word itself
2. POS tag
3. Lemmatized word
4. Is first letter Capital 
5. A feature representing the long word shape of the current token. This feature is defined by mapping any uppercase letter, lowercase letter, digit, and other characters to X x 0 and O respectively. For example, the long word shape of ”d-glycericacidemia” is xOxxxxxxxxxxxxxxxx.
6. A feature representing the brief word class of the current token. In this feature, consecutive uppercase letters, lowercase letters, digits, and other characters map to X, x, 0, and O, respectively. For example, the brief word shape of ”d-glycericacidemia” is xOx.
7. A feature representing the type of token: word, number, symbol or punctuation.
