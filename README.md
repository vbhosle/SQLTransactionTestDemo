# Pre-requisite
Use Java 17 and above.

# Goal
Reliably reproduce concurrent transactions scenario in developer test.

# Approach
Get version of existing record, increment it by one and save. 
Multiple transactions will try to do the same.
If we successfully manage to co-ordinate the transactions to read the same version and increment it by one, then we have successfully reproduced the scenario.

# How to run tests
We used intellij idea's test configuration to repeat test until failure. 

# Result
For expected failure scenarios, tests failed within 1000 iterations. If it is taking too long, maybe stop and retry before your intellij hangs up. Or suggest us a better method.

# Note
Transaction hook can help you gain visibility inside the transaction stages.

Example:
```
TransactionSynchronizationManager.registerSynchronization(
    new TransactionSynchronization() {
        @Override
        public void beforeCommit(boolean readOnly) {
        System.out.println(Thread.currentThread().getName() + " beforeCommit");
    }
});
```
