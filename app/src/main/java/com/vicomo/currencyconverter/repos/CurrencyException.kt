package com.vicomo.currencyconverter.repos

import java.lang.RuntimeException

class CurrencyException(msg: String?):
    RuntimeException(msg)