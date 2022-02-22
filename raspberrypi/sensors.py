try:
    import explorerhat
    print("Import successful")
except:
    print("Import failed")

def readAnalog(value):
    if value == 4:
        return explorerhat.analog.four.read()
    elif value == 3:
        return explorerhat.analog.three.read()
    elif value == 2:
        return explorerhat.analog.two.read()
    elif value == 1:
        return explorerhat.analog.one.read()
    else:
        return 0
