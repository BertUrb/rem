package com.openclassrooms.realestatemanager.database.dao;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.database.Cursor;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.openclassrooms.realestatemanager.TestLifecycleOwner;
import com.openclassrooms.realestatemanager.database.SaveRealEstateDB;
import com.openclassrooms.realestatemanager.model.RealEstate;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class RealEstateDaoTest extends TestCase {

    private RealEstateDao dao;
    private SaveRealEstateDB testDb;
    private final TestLifecycleOwner lifecycleOwner = new TestLifecycleOwner();

    public void testGetAllRealEstate() throws Throwable {
        runOnUiThread(() -> dao.getAllRealEstate().observe(lifecycleOwner, realEstates -> assertEquals(5,realEstates.size())));

    }

    public void testCreateOrUpdateRealEstate() throws Throwable {
        // Create a mock RealEstate object
        RealEstate mockRealEstate = new RealEstate();
        dao.createOrUpdateRealEstate(mockRealEstate);

        // Mock the behavior of the DAO method
        runOnUiThread(() -> {
            dao.getAllRealEstate().observe(lifecycleOwner,realEstates -> assertEquals(6,realEstates.size()));
        });

    }

    public void testDeleteRealEstate() throws Throwable {
        RealEstate mockRealEstate = new RealEstate();
        mockRealEstate.setID(1L);

       dao.deleteRealEstate(mockRealEstate);

        runOnUiThread(() -> {
            dao.getAllRealEstate().observe(lifecycleOwner,realEstates -> assertEquals(4,realEstates.size()));
        });
    }

    public void testInsertMultipleRealEstates() throws Throwable {
        List<RealEstate> estates = new ArrayList<>();
        estates.add(new RealEstate());
        estates.add(new RealEstate());

        // Call the method to insert multiple real estates
        dao.insertMultipleRealEstates(estates);

        runOnUiThread(() -> {
            dao.getAllRealEstate().observe(lifecycleOwner,realEstates -> assertEquals(7,realEstates.size()));
        });

    }

    public void testGetRealEstateWithCursor() throws Throwable {


        runOnUiThread(() -> {
            dao.getAllRealEstate().observe(lifecycleOwner,realEstates -> {
                assertEquals(5,realEstates.size());
                Cursor res = dao.getRealEstateWithCursor(realEstates.get(0).getID());
                assertEquals(realEstates.get(0).getID(),res.getLong(res.getColumnIndex("mID")));
            });
        });



    }

    public void testFilterRealEstates() throws Throwable {
        // Create mock input parameters
        String name = "example";
        Date maxSaleDate = new Date();
        Date minListingDate = new Date();
        int maxPrice = 100000;
        int minPrice = 50000;

        int maxSurface = 2000;
        int minSurface = 1000;




        runOnUiThread(()-> {
            dao.filterRealEstates(name, maxSaleDate, minListingDate, maxPrice,
                    minPrice, maxSurface, minSurface).observe(lifecycleOwner, realEstates -> {
                assert realEstates != null;
                assertEquals(1, realEstates.size());
            });
        });





}

    @Override
    public void setUp() throws Exception {
        super.setUp();

        testDb = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), SaveRealEstateDB.class).build();

        dao = testDb.realEstateDao();

        List<RealEstate> estates = new ArrayList<>();

        estates.add(new RealEstate("example",  "region",  "location",  "description",  "featuredMediaUrl", 60000, 1500, 3, 1, 2, null ));
        estates.add(new RealEstate("example2",  "region",  "location",  "description",  "featuredMediaUrl", 60000, 1500, 3, 1, 2, null ));

        dao.insertMultipleRealEstates(estates);
    }
    @Override
    protected void tearDown() throws Exception {
        testDb.close();
        super.tearDown();
    }
}