# HLS-Validator
HTTP HLS Validator

A program to validate a file with the HLS protocol. A report containing the errors and the line where they
occurred will be printed when finished. A text file of URI's can be used to execute in batch mode.

Visitor pattern is used to handle multiple ways to validate the file such as by Duration and the URI
