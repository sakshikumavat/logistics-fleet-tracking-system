/**
 * Initial Shipments & Freight Consignments
 */
export const initialShipments = [
  {
    id: 'CN-88420-IN',
    client: 'Tata Motors Supply Chain Ltd',
    vehiclePlate: 'MH12AB1234',
    driver: 'Ramesh Patel',
    cargo: 'Engine Assemblies (22.4T)',
    origin: 'Pune Depot',
    destination: 'Mumbai JNPT Port',
    status: 'In Transit',
    eta: '16:30 IST',
    priority: 'High'
  },
  {
    id: 'CN-88435-IN',
    client: 'Serum Biopharma Logistics',
    vehiclePlate: 'MH14DE5678',
    driver: 'Amit Shinde',
    cargo: 'Vaccines Cold Chain (14.2T)',
    origin: 'Pune Chakan Hub',
    destination: 'Panvel Hub',
    status: 'In Transit',
    eta: '15:45 IST',
    priority: 'Critical'
  },
  {
    id: 'CN-88490-IN',
    client: 'Hindustan Consumer Logistics',
    vehiclePlate: 'MH04FK9012',
    driver: 'Vikram Rao',
    cargo: 'FMCG Packaged Goods (18.6T)',
    origin: 'Dehu Yard',
    destination: 'Nashik MIDC Terminal',
    status: 'Refueling Stop',
    eta: '19:15 IST',
    priority: 'Normal'
  }
];

export default initialShipments;
