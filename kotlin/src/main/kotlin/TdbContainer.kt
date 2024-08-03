package com.github.davidkleiven.pyjena
import org.apache.jena.tdb2.TDB2Factory

class TdbContainer {
    val ds = TDB2Factory.createDataset()
}
