package com.tekkr.data.internal.common

import java.io.IOException

class ApiException(message: String) : IOException(message)
class RiderLoginException() : IOException()