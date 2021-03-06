package org.cryptocoinpartners.schema;

import org.cryptocoinpartners.util.PersistUtil;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents the possibility to trade one Fungible for another
 */
@SuppressWarnings( "UnusedDeclaration" )
@Entity
public class Listing extends BaseEntity
{
    @ManyToOne(optional = false)
    public Fungible getBase() { return base; }


    @ManyToOne(optional = false)
    public Fungible getQuote() { return quote; }


    /** will create the listing if it doesn't exist */
    public static Listing forPair( Fungible base, Fungible quote ) {
        try {
            Listing listing = PersistUtil.queryZeroOne(Listing.class,
                                                       "select a from Listing a where base=?1 and quote=?2",
                                                       base, quote);
            if( listing == null ) {
                listing = new Listing(base,quote);
                PersistUtil.insert(listing);
            }
            return listing;
        }
        catch( NoResultException e ) {
            final Listing listing = new Listing(base, quote);
            PersistUtil.insert(listing);
            return listing;
        }
    }


    public String toString() { return getSymbol(); }


    @Transient
    public String getSymbol() { return base.getSymbol()+'.'+quote.getSymbol(); }


    public static List<String> allSymbols() {
        List<String> result = new ArrayList<>();
        List<Listing> listings = PersistUtil.queryList(Listing.class, "select x from Listing x");
        for( Listing listing : listings )
            result.add((listing.getSymbol()));
        return result;
    }


    // JPA
    protected Listing() { }
    protected void setBase(Fungible base) { this.base = base; }
    protected void setQuote(Fungible quote) { this.quote = quote; }


    protected Fungible base;
    protected Fungible quote;


    private Listing( Fungible base, Fungible quote ) {
        this.base = base;
        this.quote = quote;
    }

    
    public static Listing forSymbol( String symbol )
    {
        final int dot = symbol.indexOf('.');
        if( dot == -1 )
            throw new IllegalArgumentException("Invalid Listing symbol: \""+symbol+"\"");
        final String baseSymbol = symbol.substring(0, dot);
        Fungible base = Fungible.forSymbol(baseSymbol);
        if( base == null )
            throw new IllegalArgumentException("Invalid base symbol: \""+baseSymbol+"\"");
        final String quoteSymbol = symbol.substring(dot + 1, symbol.length());
        Fungible quote = Fungible.forSymbol(quoteSymbol);
        if( quote == null )
            throw new IllegalArgumentException("Invalid quote symbol: \""+quoteSymbol+"\"");
        return Listing.forPair(base,quote);
    }
}
