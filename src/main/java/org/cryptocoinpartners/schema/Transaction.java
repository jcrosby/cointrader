package org.cryptocoinpartners.schema;

import org.joda.time.Duration;

import javax.persistence.Entity;


/**
 * A Transaction represents the modification of multiple Positions, whether it is a purchase on a Market or an Account
 * transfer.
 * of Fungibles between Accounts
 * @author Tim Olson
 */
@Entity
public class Transaction extends BaseEntity {


    enum TransactionStatus { OFFERED, ACCEPTED, CLOSED, SETTLED, CANCELLED }

     // todo add basis rounding

    private Duration estimatedDelay;
}
