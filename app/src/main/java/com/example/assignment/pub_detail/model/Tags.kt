package com.example.assignment.pub_detail.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tags(
    val access: String? = null,
    @SerialName("addr:city")
    val addrCity: String? = null,
    @SerialName("addr:conscriptionnumber")
    val addrConscriptionnumber: String? = null,
    @SerialName("addr:country")
    val addrCountry: String? = null,
    @SerialName("addr:floor")
    val addrFloor: String? = null,
    @SerialName("addr:housename")
    val addrHousename: String? = null,
    @SerialName("addr:housenumber")
    val addrHousenumber: String? = null,
    @SerialName("addr:postcode")
    val addrPostcode: String? = null,
    @SerialName("addr:street")
    val addrStreet: String? = null,
    @SerialName("addr:streetnumber")
    val addrStreetnumber: String? = null,
    @SerialName("addr:suburb")
    val addrSuburb: String? = null,
    @SerialName("alt_name")
    val altName: String? = null,
    val amenity: String? = null,
    val bar: String? = null,
    @SerialName("beer_garden")
    val beerGarden: String? = null,
    val brewery: String? = null,
    val capacity: String? = null,
    @SerialName("check_date")
    val checkDate: String? = null,
    val club: String? = null,
    val complete: String? = null,
    @SerialName("contact:email")
    val contactEmail: String? = null,
    @SerialName("contact:facebook")
    val contactFacebook: String? = null,
    @SerialName("contact:instagram")
    val contactInstagram: String? = null,
    @SerialName("contact:phone")
    val contactPhone: String? = null,
    @SerialName("contact:twitter")
    val contactTwitter: String? = null,
    @SerialName("contact:website")
    val contactWebsite: String? = null,
    val cuisine: String? = null,
    val description: String? = null,
    @SerialName("disused:amenity")
    val disusedAmenity: String? = null,
    @SerialName("disused:name")
    val disusedName: String? = null,
    @SerialName("drink:kofola")
    val drinkKofola: String? = null,
    @SerialName("drink:wine")
    val drinkWine: String? = null,
    val email: String? = null,
    val fixme: String? = null,
    val food: String? = null,
    val indoor: String? = null,
    @SerialName("indoor_seating")
    val indoorSeating: String? = null,
    @SerialName("internet_access")
    val internetAccess: String? = null,
    @SerialName("internet_access:fee")
    val internetAccessFee: String? = null,
    val layer: String? = null,
    val leisure: String? = null,
    val level: String? = null,
    val microbrewery: String? = null,
    val min_age: String? = null,
    val name: String? = null,
    @SerialName("name:en")
    val nameEn: String? = null,
    val note: String? = null,
    @SerialName("official_name")
    val officialName: String? = null,
    @SerialName("old_name")
    val oldName: String? = null,
    @SerialName("opening_hours")
    val openingHours: String? = null,
    @SerialName("opening_hours:covid19")
    val openingHoursCovid19: String? = null,
    @SerialName("operator")
    val operator: String? = null,
    @SerialName("outdoor_seating")
    val outdoorSeating: String? = null,
    @SerialName("payment:account_cards")
    val paymentAccountCards: String? = null,
    @SerialName("payment:cash")
    val paymentCash: String? = null,
    @SerialName("payment:credit_cards")
    val paymentCreditCards: String? = null,
    @SerialName("payment:debit_cards")
    val paymentDebitCards: String? = null,
    @SerialName("payment:diners_club")
    val paymentDinersClub: String? = null,
    @SerialName("payment:discover_card")
    val paymentDiscoverCard: String? = null,
    @SerialName("payment:jcb")
    val paymentJcb: String? = null,
    @SerialName("payment:maestro")
    val paymentMaestro: String? = null,
    @SerialName("payment:mastercard")
    val paymentMastercard: String? = null,
    @SerialName("payment:visa")
    val paymentVisa: String? = null,
    @SerialName("payment:visa_debit")
    val paymentVisaDebit: String? = null,
    @SerialName("payment:visa_electron")
    val paymentVisaElectron: String? = null,
    val phone: String? = null,
    val reservation: String? = null,
    val shop: String? = null,
    val smoking: String? = null,
    val source: String? = null,
    @SerialName("source:amenity")
    val sourceAmenity: String? = null,
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("survey:date")
    val surveyDate: String? = null,
    @SerialName("toilets:wheelchair")
    val toiletsWheelchair: String? = null,
    val url: String? = null,
    val website: String? = null,
    val wheelchair: String? = null
)