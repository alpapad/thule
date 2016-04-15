package uk.co.serin.thule.people.datafactories;

/**
 * Holds all the repositories data factories.
 */
public class DataFactory {
    private final ReferenceDataFactory referenceDataFactory;
    private final TestDataFactory testDataFactory;

    public DataFactory(ReferenceDataFactory referenceDataFactory) {
        this.referenceDataFactory = referenceDataFactory;
        this.testDataFactory = new TestDataFactory(this);
    }

    public ReferenceDataFactory getReferenceDataFactory() {
        return referenceDataFactory;
    }

    public TestDataFactory getTestDataFactory() {
        return testDataFactory;
    }
}
