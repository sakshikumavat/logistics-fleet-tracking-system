# FleetTrack Logistics OS — Smart Logistics & Fleet Management Portal

[![Vite](https://img.shields.io/badge/Vite-6.2.0-646CFF?style=flat&logo=vite&logoColor=white)](https://vitejs.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.4-38B2AC?style=flat&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)
[![Leaflet](https://img.shields.io/badge/Leaflet-1.9.4-199900?style=flat&logo=leaflet&logoColor=white)](https://leafletjs.com/)
[![Standard](https://img.shields.io/badge/Compliance-AIS--140-004AC6?style=flat)]()
[![Author](https://img.shields.io/badge/Author-Sakshi_Kumavat-0F172A?style=flat)]()

An enterprise-grade, high-performance fleet operations and telematics dashboard engineered with modern responsive design, Plus Jakarta Sans typography, and real-time monitoring telemetry.

---

## 🏛️ Enterprise Project Architecture

```
frontend/
├── 📁 .vscode/                  # Pre-configured launch, debuggers, tasks & formatters
├── 📁 docs/                     # 🎓 Industrial Presentation & System Documentation
│   ├── ARCHITECTURE.md          # Complete C4 System Architecture & Tech Stack Details
│   ├── TELEMATICS_DATAFLOW.md   # AIS-140 & OBD-II Telemetry Packet Specs & Dataflow
│   └── PRESENTATION_GUIDE.md    # 5-Minute Viva / Client Walkthrough Script & Q&A
├── 📁 public/                   # Static public assets & realistic mock feeds
│   ├── 📁 data/                 # JSON Mock Telemetry Feeds
│   │   ├── fleet-telemetry.json # Raw OBD-II CAN-bus & GPS stream
│   │   ├── shipments-feed.json  # Freight manifest & dispatch feed
│   │   └── driver-compliance.json# Commercial driver licenses & rosters
│   ├── favicon.svg              # FleetTrack brand vector logo
│   └── manifest.json            # Web App Manifest (PWA)
├── 📁 src/                      # 🧱 Modular Enterprise Source Layer
│   ├── 📁 config/               # App configuration & theme tokens
│   │   ├── theme.js             # Consolidated design tokens & Tailwind theme
│   │   └── appConfig.js         # Hubs, polling intervals & map configuration
│   ├── 📁 data/                 # Seed datasets (vehicles, shipments, drivers)
│   ├── 📁 modules/              # Core business domain logic
│   │   ├── auth.js              # Enterprise auth gate & session token manager
│   │   ├── telematics.js        # Vehicle telematics & corridor calculations
│   │   └── mapEngine.js         # Leaflet tile layers & custom marker engine
│   ├── 📁 services/             # Storage abstractions & telemetry simulator
│   ├── 📁 styles/               # Modular CSS stylesheets (main, scrollbars, map)
│   └── 📁 utils/                # Date, currency (₹), geospatial (Haversine), and DOM helpers
├── 📄 index.html                # Operations Command Dashboard
├── 📄 fleet.html                # Commercial Vehicle Fleet Roster
├── 📄 add-vehicle.html          # New Commercial Vehicle Registration
├── 📄 drivers.html              # Driver Compliance & Roster Directory
├── 📄 shipments.html            # Active Consignments & Freight Manifests
├── 📄 create-shipment.html      # Freight Dispatch Scheduler
├── 📄 live-tracking.html        # Real-Time GPS Telematics Corridor Map
├── 📄 reports.html              # Fleet Intelligence, Fuel & Performance Reports
├── 📄 settings.html             # Telematics Preferences & API Gateway Settings
├── 📄 login.html                # Operations & Dispatcher Authentication Terminal
├── 📄 signup.html               # Enterprise Fleet Onboarding Portal
├── ⚙️ vite.config.js             # Production-grade Vite MPA configuration with '@' alias
├── ⚙️ package.json              # Project scripts and dependencies
├── ⚙️ .editorconfig             # Standard code formatting specification
└── ⚙️ .prettierrc               # Prettier code style rules
```

---

## 🚀 Quick Start

### Option 1: Run via Node / NPM (Recommended)

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Start the local development server**:
   ```bash
   npm run dev
   ```
   or:
   ```bash
   npm start
   ```
   This will start Vite and automatically open:
   👉 **http://localhost:3000** (Defaulting to the login gate)

3. **Build for Production**:
   ```bash
   npm run build
   ```

4. **Preview Production Build**:
   ```bash
   npm run preview
   ```

---

### Option 2: Run in Visual Studio Code / Live Server

1. **Open the folder** in VS Code:
   - File -> Open Folder -> Select `frontend`
2. **Press F5** or go to `Run and Debug` panel:
   - Select **Launch Chrome (FleetTrack Dev Server)** or **Launch Edge (FleetTrack Dev Server)**
3. **Using Live Server Extension**:
   - Right-click `index.html` or `login.html` -> Click **"Open with Live Server"**

---

## 📁 Pages & Navigation Structure

All 11 pages are cleanly organized at the root for zero 404 risk, active navigation, and 100% verified UI state integrity:

| Page | File | Description |
|---|---|---|
| **Dashboard** | [`index.html`](file:///./index.html) | Central Operations Command, telematics feed, and KPI metrics |
| **Fleet** | [`fleet.html`](file:///./fleet.html) | Commercial vehicle fleet assets, status, and maintenance rosters |
| **Add Vehicle** | [`add-vehicle.html`](file:///./add-vehicle.html) | New commercial vehicle registration with telematics sync |
| **Drivers** | [`drivers.html`](file:///./drivers.html) | Commercial drivers directory, compliance status & certifications |
| **Shipments** | [`shipments.html`](file:///./shipments.html) | Freight dispatch, active consignments & transit timelines |
| **Create Shipment** | [`create-shipment.html`](file:///./create-shipment.html) | Consignment ingest form & hub dispatch scheduler |
| **Live Tracking** | [`live-tracking.html`](file:///./live-tracking.html) | Live GPS telematics interactive map & fleet grid |
| **Reports & Analytics** | [`reports.html`](file:///./reports.html) | Fleet intelligence, delivery performance & fuel expenditure |
| **Settings** | [`settings.html`](file:///./settings.html) | Telematics preferences, security, notifications & API gateways |
| **Login** | [`login.html`](file:///./login.html) | Operations & dispatcher authentication terminal |
| **Sign Up** | [`signup.html`](file:///./signup.html) | Fleet registration & enterprise onboarding portal |

---

## 🎓 Industrial Presentation Quick Links

- 📘 [**Full System Architecture (`docs/ARCHITECTURE.md`)**](file:///./docs/ARCHITECTURE.md): Complete C4 Container diagrams, IoT telematics pipeline, database schemas, and microservice topology.
- 📡 [**Telematics Dataflow Specification (`docs/TELEMATICS_DATAFLOW.md`)**](file:///./docs/TELEMATICS_DATAFLOW.md): Real-time OBD-II/AIS-140 JSON schemas, sequence diagrams, and geofence state machines.
- 🎤 [**Viva & Presentation Guide (`docs/PRESENTATION_GUIDE.md`)**](file:///./docs/PRESENTATION_GUIDE.md): 5-minute presentation script, key talking points, and model answers for examiner questions.

---

## 🎨 UI & Design System Integrity

- **Visual State**: 100% authentic to original design specifications with zero visual alterations.
- **Typography**: Plus Jakarta Sans via Google Fonts.
- **Styling**: Tailwind CSS & modular styles in `src/styles/`.
- **Iconography**: Google Material Symbols Outlined & Phosphor Icons.
- **Map Visualizations**: Leaflet 1.9.4 with CartoDB Voyager tiles and custom animated radar pulse markers.
- **Session Security**: AIS-140 client auth protection gate.

---

## 👤 Author & Credits

- **Author**: Sakshi Kumavat
- **Project**: FleetTrack Logistics OS
- **License**: ISC
