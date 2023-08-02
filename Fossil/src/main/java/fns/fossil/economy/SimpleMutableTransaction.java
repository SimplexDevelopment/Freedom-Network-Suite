package fns.fossil.economy;

import fns.patchwork.economy.EconomicEntity;
import fns.patchwork.economy.MutableTransaction;

public class SimpleMutableTransaction extends SimpleTransaction implements MutableTransaction
{
    public SimpleMutableTransaction(final EconomicEntity source, final EconomicEntity destination, final long balance)
    {
        super(source, destination, balance);
    }

    @Override
    public long addToBalance(final long amount)
    {
        return balance.addAndGet(amount);
    }

    @Override
    public long removeFromBalance(final long amount)
    {
        return this.addToBalance(-amount);
    }

    @Override
    public void setBalance(final long newBalance)
    {
        balance.set(newBalance);
    }
}
