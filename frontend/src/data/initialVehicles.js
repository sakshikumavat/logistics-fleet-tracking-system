/**
 * Initial Vehicle Inventory Dataset
 */
export const initialVehicles = [
  {
    id: 'VEH-1042',
    plate: 'MH12AB1234',
    type: 'Heavy Commercial (28T Multi-Axle)',
    model: 'Tata Prima 4028.S',
    status: 'In Transit',
    driver: 'Ramesh Patel',
    fuelLevel: 76,
    speed: 68,
    route: 'Pune -> Mumbai JNPT',
    odometer: 142850,
    health: 'Good'
  },
  {
    id: 'VEH-1055',
    plate: 'MH14DE5678',
    type: 'Refrigerated Cold Chain Rig',
    model: 'BharatBenz 3523R',
    status: 'In Transit',
    driver: 'Amit Shinde',
    fuelLevel: 61,
    speed: 72,
    route: 'Pune Chakan -> Panvel Depot',
    odometer: 98420,
    health: 'Optimal'
  },
  {
    id: 'VEH-1089',
    plate: 'MH04FK9012',
    type: 'Haulage Heavy Transport',
    model: 'Ashok Leyland 2820',
    status: 'Maintenance / Stopped',
    driver: 'Vikram Rao',
    fuelLevel: 88,
    speed: 0,
    route: 'Dehu Yard -> Nashik Terminal',
    odometer: 218900,
    health: 'Service Required'
  }
];

export default initialVehicles;
