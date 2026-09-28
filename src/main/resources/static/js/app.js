const API_BASE = "";


// =====================================================
// PAGE LOAD
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    loadFoodListings();
    loadDashboard();
    loadMonthlySummary();

    const foodForm =
        document.getElementById("foodForm");

    if (foodForm) {
        foodForm.addEventListener(
            "submit",
            createFoodListing
        );
    }
});


// =====================================================
// NAVIGATION
// =====================================================

function showSection(sectionId) {

    const section =
        document.getElementById(sectionId);

    if (section) {

        section.scrollIntoView({
            behavior: "smooth"
        });
    }
}


// =====================================================
// LOAD AVAILABLE FOOD LISTINGS
// =====================================================

async function loadFoodListings() {

    const container =
        document.getElementById("listingContainer");

    if (!container) {
        return;
    }

    container.innerHTML =
        "<p>Loading food listings...</p>";

    try {

        /*
         * Your controller:
         *
         * GET /api/food-listings/available
         */

        const response =
            await fetch(
                API_BASE +
                "/api/food-listings/available"
            );


        if (!response.ok) {

            throw new Error(
                "Unable to load food listings"
            );
        }


        const listings =
            await response.json();


        container.innerHTML = "";


        if (!listings ||
            listings.length === 0) {

            container.innerHTML = `
                <div class="listing-card">

                    <h3>
                        No food available
                    </h3>

                    <p>
                        There are currently no
                        available food listings.
                    </p>

                </div>
            `;

            return;
        }


        listings.forEach(function (listing) {

            container.innerHTML +=
                createListingCard(listing);

        });


    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <div class="listing-card">

                <h3>
                    Unable to load listings
                </h3>

                <p>
                    Please make sure the
                    Spring Boot application
                    is running.
                </p>

            </div>
        `;
    }
}


// =====================================================
// CREATE FOOD LISTING CARD
// =====================================================

function createListingCard(listing) {

    const safeUntil =
        listing.safeUntil
            ? formatDate(listing.safeUntil)
            : "Not specified";


    return `
        <div class="listing-card">

            <div class="listing-type">
                ${escapeHtml(
        listing.foodType
    )}
            </div>

            <h3>
                ${escapeHtml(
        listing.foodName
    )}
            </h3>


            <div class="listing-info">

                <span>
                    <strong>
                        Quantity:
                    </strong>

                    ${listing.quantity}

                    ${escapeHtml(
        listing.unit
    )}
                </span>


                <span>
                    <strong>
                        Safe Until:
                    </strong>

                    ${safeUntil}
                </span>


                <span>
                    <strong>
                        Pickup:
                    </strong>

                    ${escapeHtml(
        listing.pickupLocation
    )}
                </span>


                <span>
                    <strong>
                        Status:
                    </strong>

                    ${escapeHtml(
        listing.status
    )}
                </span>

            </div>


            <button
                class="claim-btn"
                onclick="claimFood(
                    ${listing.id}
                )">

                Claim Food

            </button>

        </div>
    `;
}


// =====================================================
// CREATE FOOD LISTING
// =====================================================

async function createFoodListing(event) {

    event.preventDefault();


    const message =
        document.getElementById(
            "formMessage"
        );


    /*
     * This JSON must match
     * FoodListingRequest.java
     */

    const request = {

        foodName:
        document.getElementById(
            "foodName"
        ).value,

        foodType:
        document.getElementById(
            "foodType"
        ).value,

        quantity:
            parseFloat(
                document.getElementById(
                    "quantity"
                ).value
            ),

        unit:
        document.getElementById(
            "unit"
        ).value,

        safeUntil:
        document.getElementById(
            "safeUntil"
        ).value,

        pickupLocation:
        document.getElementById(
            "pickupLocation"
        ).value,

        donorId:
            parseInt(
                document.getElementById(
                    "donorId"
                ).value
            )
    };


    try {

        /*
         * Your controller:
         *
         * POST /api/food-listings
         */

        const response =
            await fetch(
                API_BASE +
                "/api/food-listings",
                {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(request)
                }
            );


        const data =
            await response.json();


        if (!response.ok) {

            throw new Error(
                data.message ||
                "Unable to create food listing"
            );
        }


        message.textContent =
            "Food listing created successfully!";

        message.style.color =
            "green";


        document
            .getElementById("foodForm")
            .reset();


        await loadFoodListings();

        await loadDashboard();


    } catch (error) {

        console.error(error);

        message.textContent =
            error.message;

        message.style.color =
            "red";
    }
}


// =====================================================
// CLAIM FOOD
// =====================================================

async function claimFood(
    foodListingId
) {

    const ngoId =
        prompt(
            "Enter NGO ID to claim this food:"
        );


    if (!ngoId) {

        return;
    }


    /*
     * Your ClaimController expects:
     *
     * POST /api/claims
     *
     * JSON body:
     *
     * {
     *     foodListingId: 1,
     *     ngoId: 1
     * }
     */


    const request = {

        foodListingId:
            parseInt(foodListingId),

        ngoId:
            parseInt(ngoId)
    };


    try {

        const response =
            await fetch(
                API_BASE +
                "/api/claims",
                {

                    method: "POST",

                    headers: {

                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(request)
                }
            );


        const data =
            await response.json();


        if (!response.ok) {

            throw new Error(
                data.message ||
                "Unable to claim food"
            );
        }


        alert(
            "Food claimed successfully!"
        );


        await loadFoodListings();

        await loadDashboard();


    } catch (error) {

        console.error(error);

        alert(
            error.message
        );
    }
}


// =====================================================
// LOAD DASHBOARD
// =====================================================

async function loadDashboard() {

    try {

        /*
         * GET ALL FOOD LISTINGS
         */

        const listingsResponse =
            await fetch(
                API_BASE +
                "/api/food-listings"
            );


        if (!listingsResponse.ok) {

            throw new Error(
                "Unable to load listings"
            );
        }


        const listings =
            await listingsResponse.json();


        /*
         * GET ALL CLAIMS
         */

        const claimsResponse =
            await fetch(
                API_BASE +
                "/api/claims"
            );


        if (!claimsResponse.ok) {

            throw new Error(
                "Unable to load claims"
            );
        }


        const claims =
            await claimsResponse.json();


        const totalListings =
            listings.length;


        const totalClaims =
            claims.length;


        const collectedClaims =
            claims.filter(
                function (claim) {

                    return claim.status ===
                        "COLLECTED";
                }
            ).length;


        /*
         * Top statistics
         */

        setText(
            "totalListings",
            totalListings
        );


        setText(
            "totalClaims",
            totalClaims
        );


        setText(
            "collectedClaims",
            collectedClaims
        );


        /*
         * Dashboard statistics
         */

        const availableListings =
            listings.filter(
                function (listing) {

                    return listing.status ===
                        "AVAILABLE";
                }
            ).length;


        setText(
            "dashboardListings",
            availableListings
        );


        setText(
            "dashboardClaims",
            totalClaims
        );


        setText(
            "dashboardCollected",
            collectedClaims
        );


    } catch (error) {

        console.error(
            "Dashboard error:",
            error
        );
    }
}


// =====================================================
// MONTHLY DASHBOARD
// =====================================================

async function loadMonthlySummary(
    year,
    month
) {

    /*
     * If year/month aren't provided,
     * use the current month.
     */

    const today =
        new Date();


    if (!year) {

        year =
            today.getFullYear();
    }


    if (!month) {

        month =
            today.getMonth() + 1;
    }


    try {

        /*
         * Your DashboardController:
         *
         * GET
         * /api/dashboard/monthly
         *
         * ?year=2026&month=9
         */

        const response =
            await fetch(
                API_BASE +
                "/api/dashboard/monthly" +
                "?year=" +
                year +
                "&month=" +
                month
            );


        if (!response.ok) {

            throw new Error(
                "Unable to load monthly summary"
            );
        }


        const data =
            await response.json();


        const container =
            document.getElementById(
                "monthlySummary"
            );


        if (!container) {

            return;
        }


        container.innerHTML = "";


        if (!data.totals ||
            data.totals.length === 0) {

            container.innerHTML = `
                <p>
                    No collected food data
                    for this month.
                </p>
            `;

            return;
        }


        data.totals.forEach(
            function (item) {

                container.innerHTML += `
                    <div class="monthly-item">

                        <span>
                            ${escapeHtml(
                    item.unit
                )}
                        </span>

                        <strong>
                            ${item.total}
                        </strong>

                    </div>
                `;
            }
        );


    } catch (error) {

        console.error(error);

        const container =
            document.getElementById(
                "monthlySummary"
            );


        if (container) {

            container.innerHTML = `
                <p>
                    Unable to load
                    monthly summary.
                </p>
            `;
        }
    }
}


// =====================================================
// FORMAT DATE
// =====================================================

function formatDate(dateString) {

    const date =
        new Date(dateString);


    return date.toLocaleString(
        "en-IN",
        {
            dateStyle: "medium",
            timeStyle: "short"
        }
    );
}


// =====================================================
// SET TEXT SAFELY
// =====================================================

function setText(
    elementId,
    value
) {

    const element =
        document.getElementById(
            elementId
        );


    if (element) {

        element.textContent =
            value;
    }
}


// =====================================================
// HTML ESCAPE
// =====================================================

function escapeHtml(value) {

    if (
        value === null ||
        value === undefined
    ) {

        return "";
    }


    return String(value)
        .replace(
            /&/g,
            "&amp;"
        )
        .replace(
            /</g,
            "&lt;"
        )
        .replace(
            />/g,
            "&gt;"
        )
        .replace(
            /"/g,
            "&quot;"
        )
        .replace(
            /'/g,
            "&#039;"
        );
}