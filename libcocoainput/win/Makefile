CC	= gcc.exe
CFLAGS	= -Wall -fPIC
FFLAGS	= 
OBJS	= libwincocoainput.o logger.o
LIBS	= 
TARGET	= libwincocoainput.dll
DISTDIR	= $(GIT_ROOT)/$(VERSION)/src/main/resources/win/
RM	= rm
CP	= cp

all: $(TARGET)

install: $(TARGET)
	$(CP) -f $(TARGET) $(DISTDIR)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) $(FFLAGS) $(LIBS)  -shared -o $@ $(OBJS) -limm32

.c.o:
	$(CC) $(CFLAGS) $(LIBS) -c $<

clean:
	$(RM) -f $(TARGET) $(OBJS)
