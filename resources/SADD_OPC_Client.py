import asyncio
import mysql.connector

from asyncua import Client
import time

# Database fields.
sadd = mysql.connector.connect(
    host="localhost",
    user="root",
    password="root",
    database="sadd"
)

# Database cursor.
cursor = sadd.cursor()

# Standard query to insert information into database.
sql = "INSERT INTO sensordata VALUES (%s, %s, %s, %s)"


async def main():
    async with Client(url='opc.tcp://localhost:53530/OPCUA/SimulationServer') as client:
        # Run this code every minute.
        while True:
            for device in await client.get_node("ns=3;i=1007").get_children():
                # Get device name, print it (tag)
                deviceName = await device.read_display_name()
                deviceName = deviceName.Text
                print(deviceName)

                # Enumerate fields.
                for parameter in await device.get_children():
                    parName = await parameter.read_display_name()
                    parName = parName.Text

                    value = await parameter.read_value()
                    print("\t", parName, value)

                    # Enter fields into database.
                    if parName != "value" and parName != "FC" and parName != "KVS":
                        cursor.execute("DELETE FROM sensordata WHERE tag = \"" + deviceName + "\" AND attribute = \"" + parName + "\"")
                    cursor.execute(sql, (deviceName, time.strftime('%Y-%m-%d %H:%M:%S'), parName, value))

                    # Sleep 1 second to ensure unique primary keys every time.
                    time.sleep(1)
            # Update database.
            sadd.commit()
            # Sleep 1 minute before next run
            time.sleep(60)

# Clear sensordata table.
cursor.execute("TRUNCATE TABLE sensordata")

# Start logger.
asyncio.run(main())
