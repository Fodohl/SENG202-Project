package seng202.team1.data.trashlog;

/**
 * Specific item being thrown away. Each constant (except {@link #OTHER}) matches an
 * {@code id} in trash_types.json, which holds its emission factors.
 *
 * <h2>Emission factors (kg CO2e per kg of material)</h2>
 *
 * <p><b>treatedFactor</b> - net emissions when the item is recycled (or composted for
 * food and garden waste, or landfilled where neither is possible). "Net" means the
 * emissions of collecting and reprocessing it, minus the emissions avoided because the
 * recycled material replaces making new material (plus forest carbon kept for paper).
 * This is why most values are negative.<br>
 * Source: U.S. EPA, Waste Reduction Model (WARM) v16, Management Practices Chapters,
 * Dec 2023, EPA-530-R-23-018, Exhibit 1-1.<br>
 * https://www.epa.gov/system/files/documents/2024-01/warm_management_practices_v16_dec.pdf
 *
 * <p><b>litteredFactor</b> - net emissions when the item ends up in landfill, because
 * collected litter is landfilled. Values: "Net Landfilling Emissions" column of the same
 * EPA table. Method: Scottish Government (2021), National Litter and Flytipping
 * Strategy: Strategic Environmental Assessment, Table 7-1.<br>
 * https://www.gov.scot/publications/national-litter-flytipping-consultation-strategic-environmental-assessment/pages/8/
 *
 * <p><b>Units</b> - EPA publishes metric tons CO2e per short ton. Both factors are
 * multiplied by 1000 / 907.18474 (= 1.10231) to give kg CO2e per kg. Exact conversion.
 *
 * <h2>Calculations</h2>
 * <pre>
 * kgCO2eSaved = (weightGrams / 1000) * max(0, litteredFactor - treatedFactor)
 * treeDays    = kgCO2eSaved * 60.33
 * </pre>
 *
 * <p><b>Saving</b> - the CO2e avoided by recycling or composting an item instead of it
 * ending up in landfill. Subtracting one WARM factor from another is how EPA says the
 * factors are compared. Items where the difference is 0 (non-recyclable plastics,
 * bricks, etc.) have no carbon saving; their benefit is keeping litter out of the
 * environment.
 *
 * <p><b>Tree-days</b> - U.S. EPA Greenhouse Gas Equivalencies Calculator: 36.4 lb carbon
 * per urban tree seedling over 10 years = 60.54 kg CO2, so 3652.5 days / 60.54 kg =
 * 60.33 tree-days per kg CO2e.<br>
 * https://www.epa.gov/energy/greenhouse-gas-equivalencies-calculator-calculations-and-references
 *
 * <p><b>weightGrams</b> - NOT sourced; typical weights only. Use the weight the user
 * records and treat this as a default.
 */
public enum TrashType {

    // Beverage containers
    PLASTIC_BOTTLE_500ML,
    PLASTIC_BOTTLE_1L,
    PLASTIC_BOTTLE_2L,
    PLASTIC_MILK_BOTTLE_2L,
    PLASTIC_BOTTLE_CAP,
    ALUMINIUM_CAN_330ML,
    ALUMINIUM_CAN_500ML,
    STEEL_FOOD_CAN,
    GLASS_BOTTLE_330ML,
    GLASS_WINE_BOTTLE,
    GLASS_JAR,

    // Takeaway and food service
    PLASTIC_CUP_LID,
    CARDBOARD_CUP_SLEEVE,
    PLASTIC_CUTLERY_SET,
    POLYSTYRENE_CLAMSHELL,
    PIZZA_BOX,

    // Bags, film and wrappers
    PLASTIC_SHOPPING_BAG,
    PLASTIC_BIN_LINER,
    CLING_FILM_SHEET,
    BUBBLE_WRAP_SHEET,

    // Paper and card
    NEWSPAPER,
    MAGAZINE,
    JUNK_MAIL_ITEM,
    OFFICE_PAPER_SHEET,
    OFFICE_PAPER_REAM,
    CARDBOARD_BOX_SMALL,
    CARDBOARD_BOX_LARGE,
    PHONE_BOOK,
    TEXTBOOK,

    // Food waste
    APPLE_CORE,
    BANANA_PEEL,
    VEGETABLE_PEELINGS,
    BREAD_SLICE,
    COOKED_RICE_PORTION,
    MEAT_SCRAPS,
    BEEF_SCRAPS,
    POULTRY_SCRAPS,
    DAIRY_WASTE,
    COFFEE_GROUNDS,
    FOOD_CADDY_FULL,

    // Garden waste
    GRASS_CLIPPINGS_BAG,

    // Electronics
    SMARTPHONE,
    TABLET,
    LAPTOP,
    DESKTOP_COMPUTER,
    FLAT_PANEL_MONITOR,
    CRT_MONITOR,
    KEYBOARD,
    COMPUTER_MOUSE,
    PRINTER,
    SMALL_APPLIANCE,

    // Household
    CAR_TYRE,
    CARPET_OFFCUT,

    // Construction and demolition
    TIMBER_OFFCUT,
    WOOD_FLOORING_PLANK,
    CLAY_BRICK,
    CONCRETE_RUBBLE,
    ASPHALT_SHINGLE,
    VINYL_FLOOR_TILE,
    STEEL_BEAM_OFFCUT,
    PVC_PIPE_LENGTH,

    // Bulk / mixed streams
    RECYCLING_BIN_FULL,
    GENERAL_WASTE_BAG,
    MIXED_ORGANICS_LOAD,

    OTHER;
}