import numpy as np
import matplotlib.pyplot as plt

# https://www.tutorialspoint.com/execute_matplotlib_online.php
# Define price range
P = np.linspace(0, 30, 8)  # Price from 0 to 30

# Compute demand, supply, and elasticity
Q_d = 120 - 4*P
Q_s = 2*P + 20
Q_e = np.abs(-4*P / (120 - 4*P))
elasticity = np.abs(-4*P / (120 - 4*P))

# Create figure and axis
fig, ax1 = plt.subplots()

# Plot Demand and Supply
#ax1.plot(P, Q_d, label="Demand (Qd = 120 - 4P)", color='blue')
#ax1.plot(P, Q_s, label="Supply (Qs = 2*P+40)", color='red')
ax1.plot(P, Q_d, label="Demand (Q_d)", color='blue')
ax1.plot(P, Q_s, label="Supply (Q_s)", color='red')
#ax1.plot(P, Q_e, label="Q_e", color='green')
ax1.set_xlabel("Price (P)")
ax1.set_ylabel("Quantity (Q)")
ax1.legend(loc="upper right")
ax1.grid()

# Create a second y-axis for Elasticity
ax2 = ax1.twinx()
ax2.plot(P, elasticity, label="Elasticity", color='green', linestyle='dashed')
ax2.set_ylabel("Elasticity")
ax2.axhline(y=1, color='gray', linestyle='dotted')  # Mark unit elasticity
ax1.axvline(x=15, color='gray', linestyle='dotted')  
ax2.legend(loc="lower right")

# Show the plot
plt.title("Demand, Supply, and Price Elasticity")
plt.show()