import asyncio
import mysql.connector

from asyncua import Client
import time

sadd = mysql.connector.connect(
    host="localhost",
    user="otacon",
    password="sherman",
    database="sadd"
)

cursor = sadd.cursor()

sql = "INSERT INTO sensordata VALUES (%s, %s, %s, %s)"


async def main():
    async with Client(url='opc.tcp://localhost:53530/OPCUA/SimulationServer') as client:
        while True:
            for device in await client.get_node("ns=3;i=1007").get_children():
                deviceName = await device.read_display_name()
                deviceName = deviceName.Text
                print(deviceName)
                for parameter in await device.get_children():
                    parName = await parameter.read_display_name()
                    parName = parName.Text

                    value = await parameter.read_value()
                    print("\t", parName, value)
                    if parName != "value" and parName != "FC" and parName != "KVS":
                        cursor.execute("DELETE FROM sensordata WHERE tag = \"" + deviceName + "\" AND attribute = \"" + parName + "\"")
                    cursor.execute(sql, (deviceName, time.strftime('%Y-%m-%d %H:%M:%S'), parName, value))
                    time.sleep(1)
            sadd.commit()
            time.sleep(60)

cursor.execute("TRUNCATE TABLE sensordata")
asyncio.run(main())
