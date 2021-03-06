package org.cryptocoinpartners.schema;


import org.joda.time.Instant;

import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.math.BigDecimal;


/**
 * Superclass for any MarketData which contains a price and volume, such as an Offer or a Trade
 *
 * @author Tim Olson
 */
@MappedSuperclass
public abstract class PriceData extends MarketData {

    /**
     * @param time when the pricing event originally occured
     * @param remoteKey the exchange's unique ID for the pricing event (to prevent duplicates)
     * @param market which Market this pricing is for
     * @param priceCount relative to the Market's quoteBasis
     * @param volumeCount relative to the Market's volumeBasis
     */
    public PriceData(Instant time, @Nullable String remoteKey, Market market,
                     @Nullable Long priceCount, @Nullable Long volumeCount) {
        super(time, remoteKey, market);
        this.priceCount = priceCount;
        this.volumeCount = volumeCount;
    }


    public PriceData(Instant time, @Nullable String remoteKey, Market market,
                     @Nullable BigDecimal price, @Nullable BigDecimal volume) {
        super(time, remoteKey, market);
        this.priceCount = Amount.roundedCountForBasis(price,market.getPriceBasis());
        this.volumeCount = Amount.roundedCountForBasis(volume, market.getVolumeBasis());
    }


    /**
     * @param time when the pricing event originally occured
     * @param remoteKey the exchange's unique ID for the pricing event (to prevent duplicates)
     * @param market which Market this pricing is for
     * @param priceCount relative to the Market's quoteBasis
     * @param volumeCount relative to the Market's volumeBasis
     */
    public PriceData(Instant time, Instant timeReceived, @Nullable String remoteKey, Market market,
                     @Nullable Long priceCount, @Nullable Long volumeCount) {
        super(time, timeReceived, remoteKey, market);
        this.priceCount = priceCount;
        this.volumeCount = volumeCount;
    }


    public PriceData(Instant time, Instant timeReceived, @Nullable String remoteKey, Market market,
                     @Nullable BigDecimal price, @Nullable BigDecimal volume) {
        super(time, timeReceived, remoteKey, market);
        this.priceCount = DecimalAmount.roundedCountForBasis(price, market.getPriceBasis());
        this.volumeCount = DecimalAmount.roundedCountForBasis(volume, market.getVolumeBasis());
    }


    public @Nullable Long getPriceCount() { return priceCount; }


    public @Nullable Long getVolumeCount() { return volumeCount; }


    @Transient @Nullable
    public DiscreteAmount getPrice() {
        if( priceCount == null )
            return null;
        if( price == null )
            price = new DiscreteAmount(priceCount, getMarket().getPriceBasis());
        return price;
    }


    @Transient @Nullable
    public Double getPriceAsDouble() {
        Amount price = getPrice();
        return price == null ? null : price.asDouble();
    }


    @Transient @Nullable
    public BigDecimal getPriceAsBigDecimal() {
        Amount price = getPrice();
        return price == null ? null : price.asBigDecimal();
    }


    @Transient @Nullable
    public DiscreteAmount getVolume() {
        if( volumeCount == null )
            return null;
        if( volume == null )
            volume = new DiscreteAmount(volumeCount, getMarket().getVolumeBasis());
        return volume;
    }


    @Transient @Nullable
    public Double getVolumeAsDouble() {
        Amount volume = getVolume();
        return volume == null ? null : volume.asDouble();
    }


    @Transient @Nullable
    public BigDecimal getVolumeAsBigDecimal() {
        Amount volume = getVolume();
        return volume == null ? null : volume.asBigDecimal();
    }


    // JPA
    protected PriceData() { super(); }
    protected void setPriceCount(Long priceCount) { this.priceCount = priceCount; }
    protected void setVolumeCount(Long volumeCount) { this.volumeCount = volumeCount; }


    private DiscreteAmount price;
    private DiscreteAmount volume;
    private Long priceCount;
    private Long volumeCount;
}
