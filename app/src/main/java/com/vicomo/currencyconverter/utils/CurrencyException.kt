package com.vicomo.currencyconverter.utils

import java.lang.RuntimeException

class CurrencyException(msg: String?):
    RuntimeException(msg)