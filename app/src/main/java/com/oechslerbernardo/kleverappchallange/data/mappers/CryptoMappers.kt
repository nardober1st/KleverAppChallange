package com.oechslerbernardo.kleverappchallange.data.mappers

import com.oechslerbernardo.kleverappchallange.data.local.model.CryptoEntity
import com.oechslerbernardo.kleverappchallange.data.remote.model.crypto.CryptoDto
import com.oechslerbernardo.kleverappchallange.data.remote.model.crypto.Data
import com.oechslerbernardo.kleverappchallange.data.remote.model.crypto.Quote
import com.oechslerbernardo.kleverappchallange.data.remote.model.crypto.USD
import com.oechslerbernardo.kleverappchallange.domain.model.Crypto

fun CryptoDto.toCrypto(): List<Crypto> {
    return data.map {
        Crypto(
            id = it.id,
            circulatingSupply = it.circulating_supply,
            name = it.name,
            slug = it.slug,
            symbol = it.symbol,
            totalSupply = it.total_supply,
            marketCap = it.quote.USD.market_cap,
            percentChange1h = it.quote.USD.percent_change_1h,
            percentChange24h = it.quote.USD.percent_change_24h,
            percentChange30d = it.quote.USD.percent_change_30d,
            percentChange60d = it.quote.USD.percent_change_60d,
            percentChange7d = it.quote.USD.percent_change_7d,
            percentChange90d = it.quote.USD.percent_change_90d,
            price = it.quote.USD.price,
            volume24h = it.quote.USD.volume_24h
        )
    }
}

fun Crypto.toCryptoDto(): CryptoDto {
    return CryptoDto(
        data = listOf(
            Data(
                id = id,
                circulating_supply = circulatingSupply,
                name = name,
                slug = slug,
                symbol = symbol,
                total_supply = totalSupply,
                quote = Quote(
                    USD = USD(
                        market_cap = marketCap,
                        percent_change_1h = percentChange1h,
                        percent_change_24h = percentChange24h,
                        percent_change_30d = percentChange30d,
                        percent_change_60d = percentChange60d,
                        percent_change_7d = percentChange7d,
                        percent_change_90d = percentChange90d,
                        price = price,
                        volume_24h = volume24h
                    )
                )
            )
        )
    )
}

// Mapper to convert Crypto to CryptoEntity
fun Crypto.toEntity(): CryptoEntity {
    return CryptoEntity(
        id = this.id,
        circulatingSupply = this.circulatingSupply,
        name = this.name,
        slug = this.slug,
        symbol = this.symbol,
        totalSupply = this.totalSupply,
        marketCap = this.marketCap,
        percentChange1h = this.percentChange1h,
        percentChange24h = this.percentChange24h,
        percentChange30d = this.percentChange30d,
        percentChange60d = this.percentChange60d,
        percentChange7d = this.percentChange7d,
        percentChange90d = this.percentChange90d,
        price = this.price,
        volume24h = this.volume24h
    )
}

// Mapper to convert CryptoEntity to Crypto
fun CryptoEntity.toCrypto(): Crypto {
    return Crypto(
        id = this.id,
        circulatingSupply = this.circulatingSupply,
        name = this.name,
        slug = this.slug,
        symbol = this.symbol,
        totalSupply = this.totalSupply,
        marketCap = this.marketCap,
        percentChange1h = this.percentChange1h,
        percentChange24h = this.percentChange24h,
        percentChange30d = this.percentChange30d,
        percentChange60d = this.percentChange60d,
        percentChange7d = this.percentChange7d,
        percentChange90d = this.percentChange90d,
        price = this.price,
        volume24h = this.volume24h
    )
}

fun Data.toEntity(): CryptoEntity {
    return CryptoEntity(
        id = this.id,
        circulatingSupply = this.circulating_supply,
        name = this.name,
        slug = this.slug,
        symbol = this.symbol,
        totalSupply = this.total_supply,
        marketCap = this.quote.USD.market_cap,
        percentChange1h = this.quote.USD.percent_change_1h,
        percentChange24h = this.quote.USD.percent_change_24h,
        percentChange7d = this.quote.USD.percent_change_7d,
        percentChange30d = this.quote.USD.percent_change_30d,
        percentChange60d = this.quote.USD.percent_change_60d,
        percentChange90d = this.quote.USD.percent_change_90d,
        price = this.quote.USD.price,
        volume24h = this.quote.USD.volume_24h
    )
}
