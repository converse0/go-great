package com.masuta.gogreat.domain.model
import kotlinx.serialization.*


@Serializable
data class User(
//    val id: Int? = null,
//    val name: String?=null,
    val email: String,
    val password: String,
//    val createdAt: String?=null,
//    val updatedAt: String?=null
)

/*
* {"status":true,"message":"Successfully logged in","data":{"refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySUQiOiJjYzYyZTEyYi01OTY2LTQ2YTItOGZlOS1lODYyMTMzM2UyODciLCJDdXN0b21LZXkiOiJmNTUyYjZmZmFmMDRlZTZjNDFhNTkyZDJkZjhhYjVkOWNjYWUxZmY0MTQ5ZmE5YWFhNjlmMGY3ODgyODA3YzM3IiwiS2V5VHlwZSI6InJlZnJlc2giLCJpc3MiOiJib29raXRlLmF1dGguc2VydmljZSJ9.m-2h681_0ylDkSWYdptMKcLxXhJ7luBNcW1MRzXJmFNivkisX7_lvxRbJOw1GdFupLXOuqGCnhAA-_lm_Im4LEUeoJ3jyGBYc-lkCl2IDo6Zfbcwbo54GdKyHvqWc7TpaaVl-GfKsemts5YwQmmbk8-SvhfDG0h7TqT9KtHt4CYIVOIjyImbPweyGYr3x09d4hYsrPfDeqLls_LNp22nRldpvmlmuHVeoIxI2ij902_DuPFijJDBVp11BE5fSl1NxdqdgwXaXP8u7pU2k55AvpR7gnWLh8GxHIrggkxhcKuP0FSHZnJw82Zr4iEVXY0pqjlvpJKQfzUyQsyq7_2vpw","access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySUQiOiJjYzYyZTEyYi01OTY2LTQ2YTItOGZlOS1lODYyMTMzM2UyODciLCJLZXlUeXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQ5MTIxNDI4LCJpc3MiOiJib29raXRlLmF1dGguc2VydmljZSJ9.Z-kzHoahbHmRvbCn-rUSxSYxiAp8MF8PpcDeSXN9G2TLQiiEnFe7XZhmUr4z0tJbNgIfxaehprQmTPe-8V9mvGdhJWCyeeQGFKSyTbI21_WO8KsmZ8HHbPZiJ74cDbJ-hJogatP-3xa9_OnDhlHS9mVv2cvsiADdV9iDAiTMCgiw93VLCOA7yzHQpj079F6S6G0-18EAgDHwS1KLNSjchrhqL5qQrKCqHSryEPgo_h294th3kP8wD1fVeA_ad-GknWtbLhCGfQ9Y8f3HUPojNxsC70VAd-dKC1x4wJvyzl8rg07yjaU9QFsp5tMzOBS_Omi4wZMSyLyZV6k9HT0V2A","username":""}}
* */