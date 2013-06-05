/*******************************************************************************
 * Copyright (C) 2013  Stefan Schroeder
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * Contributors:
 *     Stefan Schroeder - initial API and implementation
 ******************************************************************************/
package basics.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import basics.Service;
import basics.Service.Builder;
import basics.route.DriverImpl;
import basics.route.ServiceActivity;
import basics.route.Start;
import basics.route.TourActivities;
import basics.route.TourActivity;
import basics.route.Vehicle;
import basics.route.VehicleImpl;
import basics.route.VehicleRoute;
import basics.route.DriverImpl.NoDriver;

public class TestVehicleRoute {
	
	private VehicleImpl vehicle;
	private NoDriver driver;

	@Before
	public void doBefore(){
		vehicle = VehicleImpl.VehicleBuilder.newInstance("v").setLocationId("loc").setType(VehicleImpl.VehicleType.Builder.newInstance("yo", 0).build()).build();
		driver = DriverImpl.noDriver();
	}
	
	
	
	@Test
	public void whenBuildingEmptyRouteCorrectly_go(){
		
		VehicleRoute route = VehicleRoute.newInstance(TourActivities.emptyTour(),DriverImpl.noDriver(),VehicleImpl.noVehicle());
		assertTrue(true);
	}
	
	@Test
	public void whenBuildingEmptyRouteCorrectlyV2_go(){
		
		VehicleRoute route = VehicleRoute.emptyRoute();
		assertTrue(true);
	}
	
	@Test
	public void whenBuildingEmptyRoute_ActivityIteratorIteratesOverZeroActivities(){
		VehicleRoute route = VehicleRoute.emptyRoute();
		Iterator<TourActivity> iter = route.getTourActivities().iterator();
		int count=0;
		while(iter.hasNext()){
			iter.next();
			count++;
		}
		assertEquals(0,count);
	}
	
	@Test(expected=IllegalStateException.class)
	public void whenBuildingEmptyRoute_(){
		
		VehicleRoute route = VehicleRoute.newInstance(null,null,null);
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void whenBuildingRouteWithNonEmptyTour_throwException(){
		TourActivities tour = new TourActivities();
		tour.addActivity(ServiceActivity.newInstance(Service.Builder.newInstance("jo", 10).build()));
		VehicleRoute route = VehicleRoute.newInstance(tour,DriverImpl.noDriver(),VehicleImpl.noVehicle());
		
	}
	
	@Test
	public void whenBuildingEmptyTour_tourIterIteratesOverAnEmptyList(){
		TourActivities tour = new TourActivities();
		Vehicle v = VehicleImpl.VehicleBuilder.newInstance("v").setLocationId("loc").setType(VehicleImpl.VehicleType.Builder.newInstance("yo", 0).build()).build();
		VehicleRoute route = VehicleRoute.newInstance(tour,DriverImpl.noDriver(),v);
		Iterator<TourActivity> iter = route.getTourActivities().iterator();
		int count = 0;
		while(iter.hasNext()){
			TourActivity act = iter.next();
			count++;
		}
		assertEquals(0,count);
	}
	
	@Test
	public void whenBuildingANonEmptyTour_tourIterIteratesOverActivitiesCorrectly(){
		TourActivities tour = new TourActivities();
		tour.addActivity(Start.newInstance("", 0, 0));
		VehicleRoute route = VehicleRoute.newInstance(tour, driver, vehicle);
		Iterator<TourActivity> iter = route.getTourActivities().iterator();
		int count = 0;
		while(iter.hasNext()){
			TourActivity act = iter.next();
			count++;
		}
		assertEquals(1,count);
	}
	
	
	@Test
	public void whenBuildingANonEmptyTour2Times_tourIterIteratesOverActivitiesCorrectly(){
		TourActivities tour = new TourActivities();
		tour.addActivity(ServiceActivity.newInstance(Service.Builder.newInstance("2", 30).setLocationId("1").build()));
		VehicleRoute route = VehicleRoute.newInstance(tour, driver, vehicle);
		
		{
			Iterator<TourActivity> iter = route.getTourActivities().iterator();
			int count = 0;
			while(iter.hasNext()){
				TourActivity act = iter.next();
				count++;
			}
			assertEquals(1,count);
		}
		{
			tour.addActivity(ServiceActivity.newInstance(Service.Builder.newInstance("3", 30).setLocationId("1").build()));
			Iterator<TourActivity> iter = route.getTourActivities().iterator();
			int count = 0;
			while(iter.hasNext()){
				TourActivity act = iter.next();
				count++;
			}
			assertEquals(2,count);
		}
	}
	
	@Test
	public void whenBuildingANonEmptyTour_tourReverseIterIteratesOverActivitiesCorrectly(){
		TourActivities tour = new TourActivities();
		VehicleRoute route = VehicleRoute.newInstance(tour, driver, vehicle);
		Iterator<TourActivity> iter = route.getTourActivities().reverseActivityIterator();
		int count = 0;
		while(iter.hasNext()){
			TourActivity act = iter.next();
			count++;
		}
		assertEquals(0,count);
	}
	
	@Test
	public void whenBuildingANonEmptyTourV2_tourReverseIterIteratesOverActivitiesCorrectly(){
		TourActivities tour = new TourActivities();
		tour.addActivity(ServiceActivity.newInstance(Service.Builder.newInstance("2", 30).setLocationId("1").build()));
		VehicleRoute route = VehicleRoute.newInstance(tour, driver, vehicle);
		Iterator<TourActivity> iter = route.getTourActivities().reverseActivityIterator();
		int count = 0;
		while(iter.hasNext()){
			TourActivity act = iter.next();
			count++;
		}
		assertEquals(1,count);
	}
	
	@Test
	public void whenBuildingANonEmptyTourV3_tourReverseIterIteratesOverActivitiesCorrectly(){
		TourActivities tour = new TourActivities();
		tour.addActivity(ServiceActivity.newInstance(Service.Builder.newInstance("2", 30).setLocationId("1").build()));
		ServiceActivity del = ServiceActivity.newInstance(Service.Builder.newInstance("3", 30).setLocationId("1").build());
		tour.addActivity(del);
		VehicleRoute route = VehicleRoute.newInstance(tour, driver, vehicle);
		Iterator<TourActivity> iter = route.getTourActivities().reverseActivityIterator();
		int count = 0;
		TourActivity memAct = null;
		while(iter.hasNext()){
			TourActivity act = iter.next();
			if(count==0) memAct = act;
			count++;
		}
		assertEquals(memAct,del);	
	}
	
	@Test
	public void whenBuildingANonEmptyTourV4_tourReverseIterIteratesOverActivitiesCorrectly(){
		TourActivities tour = new TourActivities();
		tour.addActivity(ServiceActivity.newInstance(Service.Builder.newInstance("2", 30).setLocationId("1").build()));
		ServiceActivity del = ServiceActivity.newInstance(Service.Builder.newInstance("3", 30).setLocationId("1").build());
		tour.addActivity(del);
		VehicleRoute route = VehicleRoute.newInstance(tour, driver, vehicle);
		Iterator<TourActivity> iter = route.getTourActivities().reverseActivityIterator();
		int count = 0;
		TourActivity memAct = null;
		while(iter.hasNext()){
			TourActivity act = iter.next();
			if(count==0) memAct = act;
			count++;
		}
		assertEquals(memAct,del);
		assertEquals(2,count);
	}
	
	@Test
	public void whenBuildingANonEmptyTour2Times_tourReverseIterIteratesOverActivitiesCorrectly(){
		TourActivities tour = new TourActivities();
		tour.addActivity(ServiceActivity.newInstance(Service.Builder.newInstance("2", 30).setLocationId("1").build()));
		ServiceActivity del = ServiceActivity.newInstance(Service.Builder.newInstance("3", 30).setLocationId("1").build());
		tour.addActivity(del);
		VehicleRoute route = VehicleRoute.newInstance(tour, driver, vehicle);
		{
			Iterator<TourActivity> iter = route.getTourActivities().reverseActivityIterator();
			int count = 0;
			TourActivity memAct = null;
			while(iter.hasNext()){
				TourActivity act = iter.next();
				if(count==0) memAct = act;
				count++;
			}
			assertEquals(memAct,del);
			assertEquals(2,count);
		}
		{
			Iterator<TourActivity> secondIter = route.getTourActivities().reverseActivityIterator();
			int count = 0;
			TourActivity memAct = null;
			while(secondIter.hasNext()){
				TourActivity act = secondIter.next();
				if(count==0) memAct = act;
				count++;
			}
			assertEquals(memAct,del);
			assertEquals(2,count);
		}
	}

}