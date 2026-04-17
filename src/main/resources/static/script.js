const BASE_URL = "http://localhost:8080/api";

const userId = localStorage.getItem("userId");

// Redirect if not logged in
if (!userId) {
    window.location.href = "login.html";
}

// Show user info
document.getElementById("userInfo").innerText =
    "Logged in User ID: " + userId;


// Load Balance
async function loadBalance() {
    try {
        const res = await fetch(`${BASE_URL}/accounts/${userId}`);
        const data = await res.json();

        document.getElementById("balance").innerText =
            "Balance: ₹" + data.balance;

    } catch (err) {
        alert("Error loading balance");
    }
}


// Send Money
async function sendMoney() {
    const toUserId = document.getElementById("toUserId").value;
    const amount = document.getElementById("amount").value;

    const res = await fetch(
        `${BASE_URL}/transactions/transfer?fromUserId=${userId}&toUserId=${toUserId}&amount=${amount}`,
        { method: "POST" }
    );

    const msg = await res.text();
    alert(msg);

    loadBalance();
}


// Load Transactions
async function loadTransactions() {
    const res = await fetch(`${BASE_URL}/transactions/${userId}`);
    const data = await res.json();

    const list = document.getElementById("txnList");
    list.innerHTML = "";

    data.forEach(txn => {
        const li = document.createElement("li");
        li.innerText =
            `₹${txn.amount} → ${txn.status} (To: ${txn.toAccount.id})`;
        list.appendChild(li);
    });
}


// Logout
function logout() {
    localStorage.removeItem("userId");
    window.location.href = "login.html";
}