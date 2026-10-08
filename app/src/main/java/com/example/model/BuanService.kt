package com.example.model

data class BuanService(
    val id: String,
    val name: String,
    val shortDescription: String,
    val fullDescription: String,
    val category: String,
    val includes: List<String>,
    val howItWorks: List<String>,
    val iconName: String
)

object BuanServicesCatalog {
    val allServices = listOf(
        BuanService(
            id = "sea-freight",
            name = "Sea Freight",
            shortDescription = "Full container load (FCL) & less than container load (LCL) ocean freight.",
            fullDescription = "BUAN Logistics operates comprehensive sea freight solutions across major global maritime corridors, connecting Nigerian ports (Apapa, Tin Can, Onne) with European, Asian, and American trade hubs.",
            category = "Freight Solutions",
            includes = listOf(
                "FCL (Full Container Load) & LCL (Less than Container Load)",
                "Temperature-controlled refrigerated reefers",
                "Port-to-port and door-to-port bill of lading",
                "Cargo insurance & container tracking"
            ),
            howItWorks = listOf(
                "Consignment packaging and container stuffing at origin hub",
                "Export customs documentation and port dispatch",
                "Ocean transit tracking with real-time waypoint reporting",
                "Port berthing, customs clearance, and terminal release"
            ),
            iconName = "DirectionsBoat"
        ),
        BuanService(
            id = "air-freight",
            name = "Air Freight",
            shortDescription = "High-speed international and regional scheduled air cargo services.",
            fullDescription = "Priority commercial and chartered air transport connecting Lagos, Abuja, Port Harcourt, and Kano to over 160 worldwide destinations with guaranteed transit windows.",
            category = "Freight Solutions",
            includes = listOf(
                "Next-flight-out and scheduled air freight",
                "Charter flights for oversized cargo",
                "Dangerous goods (DGR) certified handling",
                "Bonded airport transit & customs processing"
            ),
            howItWorks = listOf(
                "Cargo drop-off at BUAN Airport Hub or scheduled courier pickup",
                "Security screening, weighing, volumetric calculation",
                "Loading onto direct commercial or dedicated freighter flight",
                "Destination airport intake, clearance, and recipient handover"
            ),
            iconName = "Flight"
        ),
        BuanService(
            id = "road-freight",
            name = "Road Freight",
            shortDescription = "Interstate transport and cross-border West African trucking corridors.",
            fullDescription = "Heavy haulage, distribution trucking, and regional haulage across all 36 states of Nigeria and ECOWAS transit corridors (Ghana, Benin, Togo, Côte d'Ivoire).",
            category = "Freight Solutions",
            includes = listOf(
                "Flatbed trailers, box trucks, and refrigerated fleets",
                "Interstate haulage with GPS telemetry escort",
                "Cross-border ECOWAS customs transit documents",
                "Dedicated driver assignment with proof-of-delivery"
            ),
            howItWorks = listOf(
                "Route planning, vehicle dispatch, and cargo staging",
                "Waybill issuance with unique digital tracking code",
                "Live transit monitoring across security checkpoints",
                "Direct destination delivery with consignee verification"
            ),
            iconName = "LocalShipping"
        ),
        BuanService(
            id = "express-delivery",
            name = "Express International Delivery",
            shortDescription = "Time-definite door-to-door courier service for urgent parcels & documents.",
            fullDescription = "Rapid global courier courier network delivering time-critical documents, sample merchandise, and e-commerce parcels within 1-3 business days worldwide.",
            category = "Express & Courier",
            includes = listOf(
                "Doorstep courier pickup and drop-off",
                "Automated customs clearance declaration",
                "Real-time SMS and push notification tracking",
                "Signature on delivery confirmation"
            ),
            howItWorks = listOf(
                "Book shipment online or drop off at any BUAN Hub",
                "Express priority bagging and international flight routing",
                "In-flight pre-clearance with local customs authorities",
                "Final-mile courier delivery directly to the recipient's door"
            ),
            iconName = "ElectricBolt"
        ),
        BuanService(
            id = "warehousing",
            name = "Warehousing & Storage",
            shortDescription = "Modern secure bonded warehousing, inventory management, and cross-docking.",
            fullDescription = "Strategically located logistics facilities in Lagos, Port Harcourt, and Abuja with secure 24/7 surveillance, WMS software, pallet racking, and fulfillment capability.",
            category = "Supply Chain",
            includes = listOf(
                "Secure ambient and temperature-controlled storage",
                "Barcode inventory tracking & real-time stock reporting",
                "Pick, pack, label, and order fulfillment services",
                "Cross-docking and container transshipment"
            ),
            howItWorks = listOf(
                "Cargo receipt and physical count inspection",
                "SKU barcode registration into inventory database",
                "Safe pallet storage in dedicated bay slots",
                "Order picking, repacking, and transport dispatch"
            ),
            iconName = "Warehouse"
        ),
        BuanService(
            id = "door-to-door",
            name = "Door-to-Door Delivery",
            shortDescription = "End-to-end logistics handling from sender's doorstep to final recipient.",
            fullDescription = "Hassle-free shipping where BUAN collects the goods from your premises, handles all customs, freight, and duties, and delivers directly to the destination address.",
            category = "Express & Courier",
            includes = listOf(
                "Origin address pickup and destination address drop",
                "Complete customs and duty pre-payment options",
                "Zero hub visit required by sender or receiver",
                "Full transit cargo coverage insurance"
            ),
            howItWorks = listOf(
                "Schedule a pickup appointment on the BUAN app",
                "Driver collects and provides immediate tracking receipt",
                "Freight transport through multimodal channels",
                "Recipient signs upon delivery at their door"
            ),
            iconName = "DoorFront"
        ),
        BuanService(
            id = "customs-clearance",
            name = "Customs Clearance",
            shortDescription = "Licensed customs brokerage, tariff optimization, and document clearance.",
            fullDescription = "Fast-track import/export customs clearance through Nigerian Customs Service (NCS) and international customs authorities, minimizing demurrage and port delays.",
            category = "Trade & Compliance",
            includes = listOf(
                "Form M processing, PAAR issuance, and NAFDAC/SONCAP clearance",
                "HS Code tariff classification and duty assessment",
                "Bonded warehouse release and transit permits",
                "Export documentation (NEPC, Clean Certificate of Inspection)"
            ),
            howItWorks = listOf(
                "Pre-arrival document review and compliance check",
                "Digital declaration submitted to Customs Single Window",
                "Physical inspection assistance and duty assessment settlement",
                "Prompt cargo release order (CRO) generation"
            ),
            iconName = "FactCheck"
        ),
        BuanService(
            id = "ship-agency",
            name = "Ship Agency",
            shortDescription = "Full port agency and protective agent services for international vessels.",
            fullDescription = "Reliable port agency services for ship owners, charterers, and operators calling at Nigerian ports and offshore terminals, ensuring rapid turnaround.",
            category = "Maritime Solutions",
            includes = listOf(
                "Port clearance, berth allocation, and pilotage coordination",
                "Immigration, port health, and maritime security compliance",
                "Disbursement account (D/A) management and reporting",
                "Crew changes, medical assistance, and visa arrangements"
            ),
            howItWorks = listOf(
                "Pre-arrival notification and port authority filings",
                "Vessel boarding upon arrival and documentation stamping",
                "Operations coordination during cargo offload/loading",
                "Outward port clearance and departure documentation"
            ),
            iconName = "Anchor"
        ),
        BuanService(
            id = "stevedoring",
            name = "Stevedoring",
            shortDescription = "Professional cargo loading and discharge operations at ports & terminals.",
            fullDescription = "Certified stevedoring teams and heavy terminal equipment handling bulk cargo, containers, project machinery, and breakbulk with speed and safety.",
            category = "Maritime Solutions",
            includes = listOf(
                "Container vessel crane loading and discharge",
                "Dry bulk and breakbulk cargo handling",
                "Heavy-lift project cargo rigging and securing",
                "Hatch men, winch men, and certified tally clerks"
            ),
            howItWorks = listOf(
                "Pre-stowage plan inspection and safety briefing",
                "Deployment of certified stevedoring crews and crane operators",
                "Continuous tallying and damage prevention monitoring",
                "Post-discharge cargo reconciliation and port handover"
            ),
            iconName = "PrecisionManufacturing"
        ),
        BuanService(
            id = "ship-broking",
            name = "Ship Broking",
            shortDescription = "Connecting shipowners with cargo charterers for optimal freight charters.",
            fullDescription = "Specialist commercial shipbroking services negotiating fixtures, spot voyages, and long-term contracts for dry bulk, oil tankers, and general cargo ships.",
            category = "Maritime Solutions",
            includes = listOf(
                "Market intelligence and charter freight rate benchmarking",
                "Fixture contract negotiation and charter party drafting",
                "Post-fixture voyage monitoring and demurrage claims",
                "Vessel sale & purchase advisory"
            ),
            howItWorks = listOf(
                "Cargo inquiry specification or vessel availability submission",
                "Matchmaking with verified global shipowner fleets",
                "Negotiation of commercial terms (Gencon, Asbatankvoy)",
                "Signing fixture confirmation and ongoing operational oversight"
            ),
            iconName = "Handshake"
        ),
        BuanService(
            id = "vessel-chartering",
            name = "Vessel Chartering",
            shortDescription = "Tailored voyage, time, and bareboat chartering for project cargo & commodities.",
            fullDescription = "Chartering solutions for oil & gas operators, mining conglomerates, and infrastructure contractors needing dedicated vessels for exclusive voyages.",
            category = "Maritime Solutions",
            includes = listOf(
                "Voyage charter, time charter, and contract of affreightment (COA)",
                "Barge and tugboat chartering for coastal inland waterways",
                "Platform supply vessels (PSV) and crew boats",
                "Technical pre-charter survey and vetting"
            ),
            howItWorks = listOf(
                "Assessment of cargo tonnage, draft restrictions, and laycan dates",
                "Vessel inspection, vetting, and charter contract execution",
                "Mobilization of vessel to load port",
                "Voyage execution with full operational management"
            ),
            iconName = "DirectionsBoat"
        ),
        BuanService(
            id = "ship-husbandry",
            name = "Ship Husbandry",
            shortDescription = "Comprehensive in-port support services for vessel crews and maintenance.",
            fullDescription = "On-demand offshore and in-port vessel husbandry including underwater inspections, bunker fuel delivery, fresh water supply, waste removal, and crew care.",
            category = "Maritime Solutions",
            includes = listOf(
                "Bunkering coordination and fresh water supply",
                "Underwater hull cleaning and propeller polishing",
                "Sludge, garbage, and bilge water disposal",
                "Technical spares delivery and workshop repairs"
            ),
            howItWorks = listOf(
                "Master submits husbandry requirements before berthing",
                "Coordination with accredited port service providers",
                "Safe delivery and execution alongside vessel",
                "Signed delivery notes and verified master endorsement"
            ),
            iconName = "Build"
        ),
        BuanService(
            id = "ship-chandling",
            name = "Ship Chandling",
            shortDescription = "Marine supplies, fresh provisions, deck equipment, and engine stores.",
            fullDescription = "Quality maritime supply services provisioning vessels with fresh provisions, bonded stores, deck stores (IMPA/ISSA catalogs), safety gear, and spare parts.",
            category = "Maritime Solutions",
            includes = listOf(
                "Fresh fruits, vegetables, meat, seafood, and dry provisions",
                "Deck stores: ropes, wires, valves, paints, safety equipment",
                "Engine stores: lubricants, filters, seals, gaskets, tools",
                "Bonded stores: confectionery, beverages, personal items"
            ),
            howItWorks = listOf(
                "Receipt of vessel RFQ with IMPA/ISSA code requirements",
                "Competitive quotation and sourcing from verified suppliers",
                "Packaging, cold chain maintenance, and customs clearance",
                "Direct delivery to vessel gangway with chief cook/engineer signoff"
            ),
            iconName = "ShoppingBag"
        )
    )
}
