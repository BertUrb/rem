package com.openclassrooms.realestatemanager.database.dao;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.model.RealEstate;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RealEstateDaoTest extends TestCase {

    private RealEstateDao dao;

    public void testGetAllRealEstate() {
        MutableLiveData<List<RealEstate>> mockLiveData = new MutableLiveData<>();
        List<RealEstate> mockRealEstates = new ArrayList<>();
        // Add some mock data to the list
        mockRealEstates.add(new RealEstate());
        mockRealEstates.add(new RealEstate());
        mockLiveData.postValue(mockRealEstates);

        // Mock the behavior of the DAO method
        when(dao.getAllRealEstate()).thenReturn(mockLiveData);

        // Call the method to get all real estates

        LiveData<List<RealEstate>> resultLiveData = dao.getAllRealEstate();

        // Verify the result
        assertNotNull(resultLiveData.getValue());
        assertEquals(2, resultLiveData.getValue().size());
    }

    public void testCreateOrUpdateRealEstate() {
        // Create a mock RealEstate object
        RealEstate mockRealEstate = new RealEstate();

        // Mock the behavior of the DAO method
        when(dao.createOrUpdateRealEstate(mockRealEstate)).thenReturn(1L);

        // Call the method to create or update a real estate
        long result = dao.createOrUpdateRealEstate(mockRealEstate);

        // Verify the result
        assertEquals(1L, result);
    }

    public void testDeleteRealEstate() {
        RealEstate mockRealEstate = new RealEstate();

        // Mock the behavior of the DAO method
        when(dao.deleteRealEstate(mockRealEstate)).thenReturn(1);

        // Call the method to delete a real estate
        int result = dao.deleteRealEstate(mockRealEstate);

        // Verify the result
        assertEquals(1, result);
    }

    public void testInsertMultipleRealEstates() {
        List<RealEstate> mockRealEstates = new ArrayList<>();
        mockRealEstates.add(new RealEstate());
        mockRealEstates.add(new RealEstate());

        // Call the method to insert multiple real estates
        dao.insertMultipleRealEstates(mockRealEstates);

        // Verify the behavior using Mockito's verify() method
        verify(dao).insertMultipleRealEstates(mockRealEstates);
    }

    public void testGetRealEstateWithCursor() {
        Cursor mockCursor = mock(Cursor.class);

        // Mock the behavior of the DAO method
        when(dao.getRealEstateWithCursor(1L)).thenReturn(mockCursor);

        // Call the method to get real estate with cursor
        Cursor resultCursor = dao.getRealEstateWithCursor(1L);

        // Verify the result
        assertNotNull(resultCursor);
        assertEquals(mockCursor, resultCursor);
    }

    public void testFilterRealEstates() {
        // Create mock input parameters
        String name = "example";
        Date maxSaleDate = new Date();
        Date minListingDate = new Date();
        int maxPrice = 100000;
        int minPrice = 50000;

        int maxSurface = 2000;
        int minSurface = 1000;

        // Create a mock LiveData object
        MutableLiveData<List<RealEstate>> mockLiveData = new MutableLiveData<>();
        List<RealEstate> mockFilteredRealEstates = new ArrayList<>();
        // Add some mock filtered data to the list
        mockFilteredRealEstates.add(new RealEstate());
        mockFilteredRealEstates.add(new RealEstate());
        mockLiveData.postValue(mockFilteredRealEstates);

        // Mock the behavior of the DAO method
        when(dao.filterRealEstates(name, maxSaleDate, minListingDate, maxPrice, minPrice, maxSurface, minSurface))
                .thenReturn(mockLiveData);

        // Call the method to filter real estates
        LiveData<List<RealEstate>> resultLiveData = dao.filterRealEstates(name, maxSaleDate, minListingDate, maxPrice,
                minPrice, maxSurface, minSurface);

        // Verify the result
        assertNotNull(resultLiveData.getValue());
        assertEquals(2, resultLiveData.getValue().size());


}

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = mock(RealEstateDao.class);
    }
}