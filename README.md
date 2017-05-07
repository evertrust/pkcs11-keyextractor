PKCS#11 Key Extractor
=====================

#Synopsis

This tool allow extracting RSA private keys from [PKCS#11](http://docs.oasis-open.org/pkcs11/pkcs11-curr/v2.40/cs01/pkcs11-curr-v2.40-cs01.pdf) crypto devices (HSM, smartcard, token) when the CKA_EXTRACTABLE attribute is set to TRUE.

This tool is **not intended to be used to migrate cryptographic materials** and should be used for the unique purpose of verifying if an RSA private key can be exported from an HSM.

Basically, what is does is:
* Opening a R/W session on a PKCS#11 token
* List the RSA private key(s) with the CKA_ATTRIBUTE set to TRUE
* Generate an in memory (CKA_TOKEN FALSE) AES 256 bits symmetric key
* Wrap the RSA private key(s) using teh AES key
* Export the RSA private key as PKCS#1 key in PEM format outside of the crypto device

This tool is developped in Scala and uses the following Java libraries:
* [BouncyCastle](https://www.bouncycastle.org/)
* [IAIK PKCS#11 Wrapper](http://jcewww.iaik.tu-graz.ac.at/sic/Products/Core_Crypto_Toolkits/PKCS_11_Wrapper)

#Prerequisites

To compile this tool you will need:
* A JDK 8
* sbt

#Generating the tool

Clone the github repository:
```
git clone https://github.com/jjalvado/pkcs11-pke
```

Generate the archive:
```
cd pkcs11-pke
sbt packageZipTarball
```

The archive is available under **'target/universal/pkcs-11-key-extractor-1.0.tgz'**.

#Installing the tool

Simply untar the archive:
```
tar xzvf pkcs-11-key-extractor-1.0.tgz
```

# Usage

```
PKCS11KeyExtractor 1.0
Usage: PKCS11KeyExtractor [options]

  -l, --library "PKCS#11 Library Path"
                           PKCS#11 Module Library Path (required)
  -s, --slot "PKCS#11 Slot ID"
                           PKCS#11 Slot ID (required)
  -p, --password "PKCS#11 Slot Password"
                           PKCS#11 Slot Password (required)
  -v, --verbose            Verbose Mode
  -t, --test               Test Mode
  -f, --force              Disregard the CKA_EXTRACTABLE attribute
------------------------------------------------------------

```

# Sample

```
[*] PKCS#11 Key Extractor - Release 1.0
[*] Registering PKCS#11 Module '/usr/lib64/libnethsm.so'
[*] Opening session on slot '1'
[?] Token Infos:
Label: PKING-Prod                      
Manufacturer ID: BULL S.A.S, Les Clayes, France  
Model: Proteccio#HR-S1 
Serial Number: 81610-0040000148
Random Number Generator: true
Write protected: false
Login required: true
User PIN initialized: true
Restore Key not needed: false
Clock on Token: false
Protected Authentication Path: false
Dual Crypto Operations: false
Token initialized: true
Secondary Authentication: false
User PIN-Count low: false
User PIN final Try: false
User PIN locked: false
User PIN to be changed: false
Security Officer PIN-Count low: false
Security Officer PIN final Try: false
Security Officer PIN locked: false
Security Officer PIN to be changed: false
Maximum Session Count: 32
Session Count: 0
Maximum Read/Write Session Count: 32
Read/Write Session Count: 0
Maximum PIN Length: -1
Minimum PIN Length: -1
Total Public Memory: -1
Free Public Memory: -1
Total Private Memory: -1
Free Private Memory: -1
Hardware Version: 0.04
Firmware Version: 1.28
Time: null
[*] Generating Test RSA key pair (CKA_EXTRACTABLE: TRUE, CKA_WRAP: FALSE, CKA_UNWRAP: FALSE)
[*] Generating Test RSA key pair (CKA_EXTRACTABLE: TRUE, CKA_WRAP: FALSE, CKA_UNWRAP: TRUE)
[*] Generating Test RSA key pair (CKA_EXTRACTABLE: TRUE, CKA_WRAP: TRUE, CKA_UNWRAP: FALSE)
[*] Generating Test RSA key pair (CKA_EXTRACTABLE: TRUE, CKA_WRAP: TRUE, CKA_UNWRAP: TRUE)
[*] Generating Test RSA key pair (CKA_EXTRACTABLE: FALSE, CKA_WRAP: FALSE, CKA_UNWRAP: FALSE)
[*] Generating Test RSA key pair (CKA_EXTRACTABLE: FALSE, CKA_WRAP: FALSE, CKA_UNWRAP: TRUE)
[*] Generating Test RSA key pair (CKA_EXTRACTABLE: FALSE, CKA_WRAP: TRUE, CKA_UNWRAP: FALSE)
[*] Generating Test RSA key pair (CKA_EXTRACTABLE: FALSE, CKA_WRAP: TRUE, CKA_UNWRAP: TRUE)
[*] Found 8 RSA private key(s)
[*] Generating an in memory (CKA_TOKEN: FALSE) 256 bits AES key
[*] Extracting RSA Private Key (CKA_EXTRACTABLE: true) with modulus 'BF:9D:EA:A4:C3:DE:CC:91:04:C1:BE:FC:5F:7C:9B:2B:96:8F:5F:DE:E7:B9:51:03:2D:39:1C:B5:38:DB:FE:7F:FA:BF:96:DD:F1:33:11:E6:4A:C1:4B:7B:F2:40:0A:1A:9E:57:F6:7C:F9:EF:AB:04:25:D9:04:70:E1:47:04:47:82:86:48:19:72:8A:10:1A:90:D0:5F:7A:E2:D5:ED:EE:4C:96:56:3F:78:5D:5B:97:62:50:F0:11:1A:39:24:9D:D3:A6:3A:4F:DB:6B:A8:CF:69:5F:F3:34:2B:BD:26:DD:7C:A7:5D:F4:F2:ED:B1:D6:A4:83:23:EB:90:6E:EC:5C:E5:85:E3:F2:87:50:3A:AC:E1:FC:13:9F:14:7D:CC:6C:FE:6B:D9:23:99:1E:8D:94:4A:C6:88:66:23:AF:6B:6A:7A:CD:C2:9B:B4:DA:CB:CE:A4:83:41:7E:D1:8D:FA:A1:45:47:A1:29:8F:C0:0A:E0:0F:5B:F6:55:B8:8C:D4:EC:CC:A0:D5:58:44:CB:74:32:F3:20:F7:ED:7A:E5:54:0A:E9:BF:8F:72:D1:1B:0A:F2:2B:AB:CE:10:2E:2B:C7:C5:77:1B:AE:1D:3B:F5:10:26:78:A9:E1:1C:95:0A:13:59:49:BC:5B:10:29:12:C4:92:29:AF:5D:4C:BF:FD:F8:ED'
[?] Key Information:   Object Class: Private Key
  Token: true
  Private: true
  Modifiable: false
  Label: EXTRACTABLE_NOWRAP_NOUNWRAP
  Key Type: RSA
  ID: <NULL_PTR>
  Start Date: <NULL_PTR>
  End Date: <NULL_PTR>
  Derive: false
  Local: true
  Key Generation Mechanism: CKM_RSA_PKCS_KEY_PAIR_GEN
  Allowed Mechanisms: <Attribute not present>
  Subject (DER, hex): <NULL_PTR>
  Sensitive: true
  Secondary Authentication: <Attribute not present>
  Secondary Authentication PIN Flags: <Attribute not present>
  Decrypt: false
  Sign: true
  Sign Recover: false
  Unwrap: false
  Extractable: true
  Always Sensitive: true
  Never Extractable: false
  Wrap With Trusted: false
  Unwrap Template: <Attribute not present>
  Always Authenticate: false
  Modulus (hex): bf9deaa4c3decc9104c1befc5f7c9b2b968f5fdee7b951032d391cb538dbfe7ffabf96ddf13311e64ac14b7bf2400a1a9e57f67cf9efab0425d90470e147044782864819728a101a90d05f7ae2d5edee4c96563f785d5b976250f0111a39249dd3a63a4fdb6ba8cf695ff3342bbd26dd7ca75df4f2edb1d6a48323eb906eec5ce585e3f287503aace1fc139f147dcc6cfe6bd923991e8d944ac6886623af6b6a7acdc29bb4dacbcea483417ed18dfaa14547a1298fc00ae00f5bf655b88cd4eccca0d55844cb7432f320f7ed7ae5540ae9bf8f72d11b0af22babce102e2bc7c5771bae1d3bf5102678a9e11c950a135949bc5b102912c49229af5d4cbffdf8ed
  Public Exponent (hex): 010001
  Private Exponent (hex): <Value is sensitive>
  Prime 1 (hex): <Value is sensitive>
  Prime 2 (hex): <Value is sensitive>
  Exponent 1 (hex): <Value is sensitive>
  Exponent 2 (hex): <Value is sensitive>
  Coefficient (hex): <Value is sensitive>
[*] Extracted Private Key:
-----BEGIN RSA PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC/neqkw97MkQTB
vvxffJsrlo9f3ue5UQMtORy1ONv+f/q/lt3xMxHmSsFLe/JAChqeV/Z8+e+rBCXZ
BHDhRwRHgoZIGXKKEBqQ0F964tXt7kyWVj94XVuXYlDwERo5JJ3TpjpP22uoz2lf
8zQrvSbdfKdd9PLtsdakgyPrkG7sXOWF4/KHUDqs4fwTnxR9zGz+a9kjmR6NlErG
iGYjr2tqes3Cm7Tay86kg0F+0Y36oUVHoSmPwArgD1v2VbiM1OzMoNVYRMt0MvMg
9+165VQK6b+PctEbCvIrq84QLivHxXcbrh079RAmeKnhHJUKE1lJvFsQKRLEkimv
XUy//fjtAgMBAAECggEBAKPXPjv18vqNgTi8ugRXgkJp8VvzN8Xf/Exdyis2W/0n
ptrNACbuGP8FqnOFnqSdPxdndS51czWSTV5bir1FQOZ4N6NMhwCANobedNWZQXy5
RqlBQ/ICOX+epEAxD3AO3BiNyMxhfJOSDbLqPBCuIp49qMoQ2OrfJf3y1/Liw8W7
Sd9kf4sfMvw2GdOPVMlj4JVGtdOwWaf5vt0RY4AEy/Cokc+S214lJe5cBf/zNYw/
1/eDvK7ab733XMLOOdnF1yYSKN4eiQ0CbyMyAWv56vAuzJltKat6EJXWt2kT+fgk
tGOjnBOsA5eKCjc3ET7A5ud3bECY0nK6CG3bc6+1SsECgYEA5JsuU1GJdRHHe46N
CilyxOGPw5SuhIBoreR6f6DiXAGf93arLzF1jvoDMGC1sibAekTHVyT5GyWd8Ljb
MLZMs5XDRVNY22Swb5vbUeyV7cIX6n3CVN54c3GLopmNwQJX8F9VUOvQXcWCvRQY
/EYrRc5sBoVH3Z4Lcx6efoQ6V50CgYEA1pQJMARGOuSzQXtbeK0X1vhgAqJTp8TW
PmG5/Dtb3m8kqRPurmqPXf95D9WBtrUMdiIMxcO0iWc4R0pia4zotpcVfBmEzO+o
Ghnc9AJxMUWsWEzbIFu/j+ZwF81IdOmbR/R/SJ9uqHxRZj8MpjOnXI+VE7P1YarJ
5BJkjd5O7ZECgYEA4pGuWyNvoZiKZbnFrTGhLuIDMysTv/zoQpg0lHOmhNuTaJaz
WKWjRp4hNPxUeAmSMFwkWQkq3q3smB0OGHxFDqO6keXmJiLn/uWwe8SIX8lDHMOh
K64GIRyC8RdQ997Tu0cw7m+Y5qcPSUg27yi4UIoiZGAJ9uphnKnXGbsBe70CgYBu
LOByyR8OSjNIIgCbe7TL6gfSEmsFr2mZ6i/GtOu80DBwqA1bfNdUGkKcwLGzkE5v
sZgBSzU9f8RDxou2UUmDxxt+9tNbLoqPrEvo9gAMnouZYwz4FmPLjTRs5DiZ3wCd
94oLTeFRnVNmPmVOSSNbwVV4ImSR21GCS3dZ6JV6IQKBgBeKLf/7a7O7dJPBlzwG
zuHXDxdRy68Ql+GVCI98maUGdhkeqzeHK2bQjeAqzOs88oZP3kvpmhV9CdudGEBA
0k1mo8gvSnbs1LH8XQ8iT69DBEwzB+1cQDU+Rm7h8J/7rfeKIDT28aAizteoloZA
OvJ/2gd7VO3cN19drXffNLWd
-----END RSA PRIVATE KEY-----

[*] Extracting RSA Private Key (CKA_EXTRACTABLE: true) with modulus 'BD:1C:4F:55:CF:F8:63:A2:50:70:98:E7:D5:05:7B:C1:CC:94:60:4D:A1:D7:D3:15:9C:8F:FE:93:8C:A1:F2:56:69:C0:55:F6:5C:03:E1:17:13:8B:BB:0E:4E:9E:96:3B:CB:BA:AF:13:F0:81:03:B7:F7:17:B5:91:0E:49:41:B4:8D:F6:ED:5C:04:D7:10:B6:A8:B1:D4:60:6E:10:21:DE:CD:0D:F3:B2:BA:89:9C:0E:64:58:6A:61:33:45:4D:04:78:70:25:89:C0:98:DF:26:28:AB:38:45:54:FA:09:6C:6A:A0:5C:03:DE:30:41:44:74:EF:23:18:0A:1B:52:85:E7:11:E0:20:A2:E5:B7:B9:F7:1A:28:A1:FE:8C:40:38:F6:66:DE:15:6D:44:01:A6:30:DE:A2:80:F6:CF:88:79:40:9D:9C:44:62:7F:D0:D9:E8:7E:5E:41:31:8D:CF:CB:44:E0:38:FD:35:CA:C6:5C:5F:3F:79:01:2E:6D:0E:D2:C7:CD:C5:E8:99:F3:95:0D:64:DF:37:A3:AB:BA:2E:09:B8:F2:F3:C9:C7:EA:82:53:6F:03:01:27:6D:FF:68:44:CE:43:BF:08:C6:CE:A2:CC:D9:A3:BF:DF:3B:F8:67:3F:99:7D:91:0D:72:F7:E5:28:14:9C:76:7A:4C:FB:EB:1D'
[?] Key Information:   Object Class: Private Key
  Token: true
  Private: true
  Modifiable: false
  Label: EXTRACTABLE_NOWRAP_UNWRAP
  Key Type: RSA
  ID: <NULL_PTR>
  Start Date: <NULL_PTR>
  End Date: <NULL_PTR>
  Derive: false
  Local: true
  Key Generation Mechanism: CKM_RSA_PKCS_KEY_PAIR_GEN
  Allowed Mechanisms: <Attribute not present>
  Subject (DER, hex): <NULL_PTR>
  Sensitive: true
  Secondary Authentication: <Attribute not present>
  Secondary Authentication PIN Flags: <Attribute not present>
  Decrypt: false
  Sign: true
  Sign Recover: false
  Unwrap: true
  Extractable: true
  Always Sensitive: true
  Never Extractable: false
  Wrap With Trusted: false
  Unwrap Template: <Attribute not present>
  Always Authenticate: false
  Modulus (hex): bd1c4f55cff863a2507098e7d5057bc1cc94604da1d7d3159c8ffe938ca1f25669c055f65c03e117138bbb0e4e9e963bcbbaaf13f08103b7f717b5910e4941b48df6ed5c04d710b6a8b1d4606e1021decd0df3b2ba899c0e64586a6133454d0478702589c098df2628ab384554fa096c6aa05c03de30414474ef23180a1b5285e711e020a2e5b7b9f71a28a1fe8c4038f666de156d4401a630dea280f6cf8879409d9c44627fd0d9e87e5e41318dcfcb44e038fd35cac65c5f3f79012e6d0ed2c7cdc5e899f3950d64df37a3abba2e09b8f2f3c9c7ea82536f0301276dff6844ce43bf08c6cea2ccd9a3bfdf3bf8673f997d910d72f7e528149c767a4cfbeb1d
  Public Exponent (hex): 010001
  Private Exponent (hex): <Value is sensitive>
  Prime 1 (hex): <Value is sensitive>
  Prime 2 (hex): <Value is sensitive>
  Exponent 1 (hex): <Value is sensitive>
  Exponent 2 (hex): <Value is sensitive>
  Coefficient (hex): <Value is sensitive>
[*] Extracted Private Key:
-----BEGIN RSA PRIVATE KEY-----
MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC9HE9Vz/hjolBw
mOfVBXvBzJRgTaHX0xWcj/6TjKHyVmnAVfZcA+EXE4u7Dk6eljvLuq8T8IEDt/cX
tZEOSUG0jfbtXATXELaosdRgbhAh3s0N87K6iZwOZFhqYTNFTQR4cCWJwJjfJiir
OEVU+glsaqBcA94wQUR07yMYChtShecR4CCi5be59xooof6MQDj2Zt4VbUQBpjDe
ooD2z4h5QJ2cRGJ/0Nnofl5BMY3Py0TgOP01ysZcXz95AS5tDtLHzcXomfOVDWTf
N6Orui4JuPLzycfqglNvAwEnbf9oRM5DvwjGzqLM2aO/3zv4Zz+ZfZENcvflKBSc
dnpM++sdAgMBAAECggEBAJUX/cBk76r1rD+pfZWn9hMrzItEiWWT6QT/TbFcfc2G
rWdTHqEa+dpssYxvUjKbtVcAvsmUotKgtnF913gQ73l/N3+ZPcOlTK0hTpQcEiHx
+26VhIKmvV9nY4GzUm2/s9eUazP4oU3FUujyu06stsNAKV+COSmIO4WJh/yK81GS
HmruKXBpSpE7xAyLAAQd+y27o74P2lt9CtJ9srkYxNvokmBpp37a0IMljgOZi/QP
D9GmTLNqGu/LmOaEvDXIc0gTuIba5+//5jgRJuCS6NzniF8VaIXWn98UwKLRk8as
6DyGZ2+dI9EYJho7ateQwtNrdFAjywjOTxL7iNU6h8ECgYEA7ILacUi2HUZ+mWQE
U/qf9zjs47FZKY9gCYUPsdBQaLgp7qKPSbuz2ZNNZqePnjPf5Uaq6m1LPEq/LZK6
GA38eLyhUcv9gdgpN5QUOSsfBgxWU2/gI4b2iib9x4fZshTFQrbGMmp08/9ERylq
4ODlzOissCy4V1PiVWqs0aSGVxECgYEAzLGNkYNEGID3JsacwwOr3S7FBLFsDPQJ
0yTBNACpI69S/VpAvPHSsfNyQNu5btcvu50IU6aCsFBjP6KPT+lldIYA5QE4SGbn
n89H8llvmlzwICOUyA0K7ZXTGH+5m3vlD4eRfT3zbqtbbSdwV5va9MXw5sQUtg7A
VuiGmVl2C00CgYEAhanWJlu/gIAMoEfkIknsCd4EExJxn4lF71th6U4fg29BU3pP
nI4WMIqNHczo37HVgVAU2/P0pxhfPzRDM8EDxIBmc0Hj/XPap2xqmXyDRfnG99Ss
SafGwYETv6ZJI9mVOgTtsiR0AEltnwILQxgXB10f61j6vW/M5BUrojdnLOECgYAC
Kucq4U75S3RSjsveLM3HG9WuRQq14GeyOrNpD5C69gJU+H2zAoSOqdtN4IXILLMu
s20BJcRAhblnVRtbZ0dRwpGD201zRtOxF4rVgCj0Y4OPc5EBxVGWTn3bxo56nQdB
8NeTkfJf+6SONVq1MstQyD/S0cS+YM2cnf2+5fmgCQKBgQCOQDCa22wR/SAclLqj
DZ5B/3P0LBZ6lIE7uRdYmIhQ0B2PnfHnoAiK7fQ8zC73UVaJc5wF5aB0HBX9ucgf
PIuMo5MU9fd3W+LvdLaEzpiiEyT1EgL16emS/SDwiN4XanFL/nW/mIODdCLqEo30
G/r0Kbdlv91saHjhqYMksLZxzg==
-----END RSA PRIVATE KEY-----

[*] Extracting RSA Private Key (CKA_EXTRACTABLE: true) with modulus 'BE:E1:4A:B2:3C:0B:54:4B:0E:C6:C4:17:14:FD:9D:40:A6:AB:02:2F:59:0D:56:9E:E3:AC:EC:00:79:35:A2:3A:28:99:8C:85:D7:C7:A7:EA:02:70:63:72:9B:78:14:55:4C:6B:F8:CF:CB:2E:ED:1C:29:26:11:5A:60:20:32:52:55:7F:FD:2E:96:28:FD:14:E3:4E:30:65:DF:D5:88:AC:F4:85:FD:AF:45:8D:68:60:60:59:1F:11:E2:F7:6F:98:C7:D7:F9:93:03:C5:29:B1:F1:C0:C6:44:B8:68:10:F0:29:90:60:8F:CA:47:72:A9:D1:8E:6D:8F:53:13:00:89:F9:F6:1B:09:BE:DC:F0:33:FE:C5:93:A3:51:78:55:47:D2:93:07:7C:64:72:14:17:AF:C1:61:6D:EA:7E:5E:77:64:93:52:9A:B4:17:78:A5:A8:24:C1:46:AA:30:3E:4A:1A:0D:9B:2A:73:78:FF:1A:18:92:2C:CB:93:C4:52:8E:4C:F7:E6:78:CE:26:6D:DE:05:48:32:88:A7:CD:12:7F:AA:0D:DB:35:0C:61:7C:52:64:4E:AD:8D:EA:87:B8:F4:A0:98:5F:C7:42:8D:EF:9E:7A:3E:FE:0D:93:06:25:00:6B:4E:62:62:00:BC:70:D4:10:E8:F8:45:9B:D3:4D:5F'
[?] Key Information:   Object Class: Private Key
  Token: true
  Private: true
  Modifiable: false
  Label: EXTRACTABLE_WRAP_NOUNWRAP
  Key Type: RSA
  ID: <NULL_PTR>
  Start Date: <NULL_PTR>
  End Date: <NULL_PTR>
  Derive: false
  Local: true
  Key Generation Mechanism: CKM_RSA_PKCS_KEY_PAIR_GEN
  Allowed Mechanisms: <Attribute not present>
  Subject (DER, hex): <NULL_PTR>
  Sensitive: true
  Secondary Authentication: <Attribute not present>
  Secondary Authentication PIN Flags: <Attribute not present>
  Decrypt: false
  Sign: true
  Sign Recover: false
  Unwrap: false
  Extractable: true
  Always Sensitive: true
  Never Extractable: false
  Wrap With Trusted: false
  Unwrap Template: <Attribute not present>
  Always Authenticate: false
  Modulus (hex): bee14ab23c0b544b0ec6c41714fd9d40a6ab022f590d569ee3acec007935a23a28998c85d7c7a7ea027063729b7814554c6bf8cfcb2eed1c2926115a60203252557ffd2e9628fd14e34e3065dfd588acf485fdaf458d686060591f11e2f76f98c7d7f99303c529b1f1c0c644b86810f02990608fca4772a9d18e6d8f53130089f9f61b09bedcf033fec593a351785547d293077c64721417afc1616dea7e5e776493529ab41778a5a824c146aa303e4a1a0d9b2a7378ff1a18922ccb93c4528e4cf7e678ce266dde05483288a7cd127faa0ddb350c617c52644ead8dea87b8f4a0985fc7428def9e7a3efe0d930625006b4e626200bc70d410e8f8459bd34d5f
  Public Exponent (hex): 010001
  Private Exponent (hex): <Value is sensitive>
  Prime 1 (hex): <Value is sensitive>
  Prime 2 (hex): <Value is sensitive>
  Exponent 1 (hex): <Value is sensitive>
  Exponent 2 (hex): <Value is sensitive>
  Coefficient (hex): <Value is sensitive>
[*] Extracted Private Key:
-----BEGIN RSA PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC+4UqyPAtUSw7G
xBcU/Z1ApqsCL1kNVp7jrOwAeTWiOiiZjIXXx6fqAnBjcpt4FFVMa/jPyy7tHCkm
EVpgIDJSVX/9LpYo/RTjTjBl39WIrPSF/a9FjWhgYFkfEeL3b5jH1/mTA8UpsfHA
xkS4aBDwKZBgj8pHcqnRjm2PUxMAifn2Gwm+3PAz/sWTo1F4VUfSkwd8ZHIUF6/B
YW3qfl53ZJNSmrQXeKWoJMFGqjA+ShoNmypzeP8aGJIsy5PEUo5M9+Z4ziZt3gVI
MoinzRJ/qg3bNQxhfFJkTq2N6oe49KCYX8dCje+eej7+DZMGJQBrTmJiALxw1BDo
+EWb001fAgMBAAECggEBAI8/hg4yj5IH7iPu/x+KoRvmuImgPOWMrxtQJ6DHVh/U
I6nwUnW4aWRtxihr1w8Lu2Pp18Uhx6q8KoleFoPzRbCSZ8X600gZfjaD9EBP9fFn
Uu7HjmTVFZLWW6u8gaeOAYtgovTYrHt5BmPUp7f4qVWC6837uyNZguY4TDJ2dtk8
ZwB0sL25PKS/j2GUtaimBy8B73XOfWTxh5Msj9g0K5l6ImgoewGq5QsJuDWm9fxq
2WY/cwGgwI7bXGr7jn2IamAVu/TR+3wbXCDLzq0/5GgP45J48uADRQLCkQim4qdy
9kS2QwLpFfGzbzadc8ZiJlAlTiHdV5J5jrMscsQPVCECgYEA48xg08IPmms5XHuI
25wORkJ71nkVO9rNmGjT0sHROvApbDxsZPPwFxpM0RvWhMZIupTO92QcLDziehV+
oDauRCsREdEQJiKApwAO790PZ04QBrz8M1i7bPtvO5U/hHzxRuotrVWyAyHZzUrl
U91m9hpDM+QYb12kYWOhvywfRe8CgYEA1oLcOuYfsVStN36XIBRRCtU2if6es8nF
iA7AAanFm9bxTdTeVn97LRmrAxdGmWUV+w428XyDhbOGRB0RYzTUFomQnS/1bEXP
ziiIySgwfR0aRuYb4T96iL0hElEngA9putxF9GwEmMfCeacj91SPEPjoll+TO4+X
bTRnZSBnX5ECgYEAuKYbOqsMQMx3c6rDNdhajcMoZKJDfwAONWOLiEQJGLBBs3WL
G43MaL2t1Lau/XNI0sz6svfYClfT8Mfpm26XdbQ/IuUHJRJEnJnkgbP7324ceYnJ
GMntKxrogDCrivZIEYaTPfS/1eAjj1ANchx+cDC7lp1m3SbXrwW2v6LBOssCgYAQ
aVacEvsdwBK4ijDZvXQt1f4kNJzDuI4qSKab+fcqYxl3EPApwfsUikAxLliyZ690
07OUaihN1FxTXb30zxfi2Yd83yxxqi2p4iQE79Q13kIiZnx/kTG4wG+FTTwuaf92
0l9vwcvgPHg+gnEX/Ros/D4fMxS6m9PEARxf+kiuYQKBgFoRwMVzmN/X9wUcCxUu
5IOjnBe+Rw9FDvnIOSPZw4SZ5QrPgtGEHNZFqjwAEvnxpsR+Q4BdA5ZX+C20NPaE
zaAcQtSpjcbyrt6fht2A0P7BBXxWAlDhSFQuB7sWMmk/132mmyGMRwwmmP4+dbUl
b5DAGoq4BzFwBww6y4k7vS4l
-----END RSA PRIVATE KEY-----

[*] Extracting RSA Private Key (CKA_EXTRACTABLE: true) with modulus 'B3:FF:49:0A:C3:0C:D1:59:20:91:2A:D9:B1:F9:5C:5D:59:78:9F:4E:AF:30:9C:DA:7D:F3:95:29:D5:BC:31:AD:89:93:FE:09:5F:25:C2:1F:52:F5:3D:D2:05:3B:E2:1D:57:7E:FE:5A:07:85:33:59:E8:E6:42:CD:6C:AF:2E:58:25:D4:29:12:DE:AA:8D:E1:B5:B3:E2:E7:D1:99:D6:A2:D7:EB:63:FA:9A:2D:59:55:76:5C:DB:B3:CE:F2:50:2C:43:5F:1B:E0:D1:EF:86:C9:AD:CC:AC:39:B2:25:3C:D1:CA:46:30:5C:01:6C:F1:29:D9:0A:CC:56:70:EF:9B:0D:D9:39:C3:5C:DA:83:16:29:4B:6B:2C:EC:D3:65:F2:FE:87:0F:97:6D:FB:1A:CD:50:F5:AF:CA:A4:97:DA:2B:84:EA:23:76:8A:C9:40:E6:43:69:44:67:67:E8:1C:44:9C:C3:2F:81:73:9B:A9:91:75:09:7F:1E:EB:2A:8C:EB:4D:7A:B5:E0:9D:70:C3:FD:36:D3:A5:0F:1A:5C:8A:C5:8F:65:4A:C2:B8:F7:36:54:B0:A9:D7:FA:0E:3B:76:70:2C:58:24:32:DE:52:78:9B:60:1A:92:62:6D:EC:19:C7:85:F4:D4:54:36:73:52:AC:78:04:15:FA:4D:17:39:EE:3D'
[?] Key Information:   Object Class: Private Key
  Token: true
  Private: true
  Modifiable: false
  Label: EXTRACTABLE_WRAP_UNWRAP
  Key Type: RSA
  ID: <NULL_PTR>
  Start Date: <NULL_PTR>
  End Date: <NULL_PTR>
  Derive: false
  Local: true
  Key Generation Mechanism: CKM_RSA_PKCS_KEY_PAIR_GEN
  Allowed Mechanisms: <Attribute not present>
  Subject (DER, hex): <NULL_PTR>
  Sensitive: true
  Secondary Authentication: <Attribute not present>
  Secondary Authentication PIN Flags: <Attribute not present>
  Decrypt: false
  Sign: true
  Sign Recover: false
  Unwrap: true
  Extractable: true
  Always Sensitive: true
  Never Extractable: false
  Wrap With Trusted: false
  Unwrap Template: <Attribute not present>
  Always Authenticate: false
  Modulus (hex): b3ff490ac30cd15920912ad9b1f95c5d59789f4eaf309cda7df39529d5bc31ad8993fe095f25c21f52f53dd2053be21d577efe5a07853359e8e642cd6caf2e5825d42912deaa8de1b5b3e2e7d199d6a2d7eb63fa9a2d5955765cdbb3cef2502c435f1be0d1ef86c9adccac39b2253cd1ca46305c016cf129d90acc5670ef9b0dd939c35cda8316294b6b2cecd365f2fe870f976dfb1acd50f5afcaa497da2b84ea23768ac940e64369446767e81c449cc32f81739ba99175097f1eeb2a8ceb4d7ab5e09d70c3fd36d3a50f1a5c8ac58f654ac2b8f73654b0a9d7fa0e3b76702c582432de52789b601a92626dec19c785f4d454367352ac780415fa4d1739ee3d
  Public Exponent (hex): 010001
  Private Exponent (hex): <Value is sensitive>
  Prime 1 (hex): <Value is sensitive>
  Prime 2 (hex): <Value is sensitive>
  Exponent 1 (hex): <Value is sensitive>
  Exponent 2 (hex): <Value is sensitive>
  Coefficient (hex): <Value is sensitive>
[*] Extracted Private Key:
-----BEGIN RSA PRIVATE KEY-----
MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCz/0kKwwzRWSCR
Ktmx+VxdWXifTq8wnNp985Up1bwxrYmT/glfJcIfUvU90gU74h1Xfv5aB4UzWejm
Qs1sry5YJdQpEt6qjeG1s+Ln0ZnWotfrY/qaLVlVdlzbs87yUCxDXxvg0e+Gya3M
rDmyJTzRykYwXAFs8SnZCsxWcO+bDdk5w1zagxYpS2ss7NNl8v6HD5dt+xrNUPWv
yqSX2iuE6iN2islA5kNpRGdn6BxEnMMvgXObqZF1CX8e6yqM6016teCdcMP9NtOl
DxpcisWPZUrCuPc2VLCp1/oOO3ZwLFgkMt5SeJtgGpJibewZx4X01FQ2c1KseAQV
+k0XOe49AgMBAAECggEAX9pOt2BvbvybjzLWgyRbv4pp5cwtFyIwaGpSwNiFQ586
aIkbzIN7ANtYpfc1ulILdhC2upLOqC31DMn+QyZ285IRjV3zHbg4vl5xMyo3VkmZ
2kx/o6MKs6w4fvcd+YQgTjBT1EF34Lt9xkTg5LFX1hB1mEMmrzEuK9rIPjAeKN8t
0eXAhG2n04X3ObuNMfPs/MgV32INnu1Fg8COAfQC8fdjhiKci/wY7rwGiZf8929K
sbzZ7kpWvH9T/VBVr/GiN98qnMg8P7hXthxDm10KEhoin03vMVO9Jo0dt0zokWnl
AyLLivmyo5eOjDKnacTRg8Yls1Vs+pcrE9MxXEGxMQKBgQDnJRrBG+p0Q1/RXLQ5
9J8oIdH3ZMIH6OoliN6mQ93wytA9nhMFtU9N2wcobAdkPR3vnqy5SqaPcuTUdjCc
g91tkibv2Um7VZWHW+NtQBaTSzknqFPYidPznMXViG4o3U5VO00J2UzfIV4HojJ+
D37TwDBdGid4EXKphRIX7Ia2OwKBgQDHWjMrB6OpqjLDCiYuXc65qlw1U6latjKc
vThxcZNULi+v714SIXVqbeYBiDJ5L2Wx53LoTNBjGy9uY1W/HusPdYgV+JBcP2CT
cvPMZDAV3ttEoRhneWYUJcnnOe+xDUn/aR6Gw/OuppZiDojOCm3RVQGNWtQNbIsx
76loyBeN5wKBgA7NdIIrbH9XlTpOKoiPnuWuDMlAHaDuDJisxD13mS4DfwSDLAqw
isycmz++c1a7B0DCM6OGIVaMcBUfIIvVcgfDVVUE4FDr8TCC9PSZJxj1t0pP4Fa8
tOMkBhNfslZRneOdcZiQ3vhnJaNtgrhJ+8BJzY8YzrBExcgPapI3poTjAoGAJdTL
Qw43E9qVuYeyiTgKJBfdWc1fpTRQVIhgLr8j+2SsbhHZ40zwMFtcu8ux8Gd7QBCx
ML5rCXO+Q4+cVSGz8AIY6jp6gtcQJ8s30wDsUyowqEx+Chzyrz4gm73V696evVag
kWicVPeOMFubwKYgAwrDuaST+1wu6jIWDylxtt8CgYBXufYbXw0ujEXzy+PJKWgD
Ll9oeylwe+ha5YydCkiLRQ7rU1i0INg4g9YhRYezUpgkLgYzYv4hMnR5poC+7BZC
h4TKW5TyqTGe3l02KaSlW0rUqdz55aC5XgjsdMc9Y4awN7LEvVemJXvIhnaHxdi4
yFUwYXuWkQABESQ4XSvSQQ==
-----END RSA PRIVATE KEY-----

[*] Extracting RSA Private Key (CKA_EXTRACTABLE: false) with modulus 'B3:58:55:C1:BE:3C:E3:84:92:11:61:AC:79:28:F3:47:DE:D8:E8:C6:C8:FA:41:9E:92:21:7D:0E:B1:CC:49:21:2E:31:46:A7:4A:D4:A6:03:5D:19:FC:11:56:30:06:80:C3:07:6D:2C:D7:58:35:7F:A2:EF:57:8C:00:D6:35:28:6B:BC:29:09:FA:88:F7:C6:2C:5A:33:C6:0E:19:91:81:9F:40:59:7C:8D:7B:BE:27:75:F3:73:2F:C3:E8:EC:E8:40:51:7D:72:A6:E2:C4:8B:DB:66:DE:E5:A6:A6:C1:85:5E:9A:50:DC:CF:9C:2D:98:97:D0:FC:E3:81:16:D7:02:45:56:80:CE:02:15:17:4B:12:43:B0:BE:6D:C1:4B:5D:7E:0E:71:85:C0:1E:02:E5:7B:C9:F2:23:89:13:E6:1E:B2:4B:D2:10:0E:88:64:72:CB:BB:5A:80:99:7B:94:64:62:93:F8:1C:B2:08:66:EE:C8:93:05:5A:77:1A:92:51:2D:8D:65:46:18:89:6E:38:40:E2:F1:E8:F0:1B:26:3B:52:1F:EB:92:36:4C:76:DE:50:84:A8:5B:6C:D4:C1:CE:25:EA:61:78:E4:8F:D8:DC:45:74:BF:1D:A5:E4:98:8A:91:42:F0:BA:F7:88:77:73:E0:32:16:E6:00:D0:A4:47'
[?] Key Information:   Object Class: Private Key
  Token: true
  Private: true
  Modifiable: false
  Label: NOEXTRACTABLE_NOWRAP_NOUNWRAP
  Key Type: RSA
  ID: <NULL_PTR>
  Start Date: <NULL_PTR>
  End Date: <NULL_PTR>
  Derive: false
  Local: true
  Key Generation Mechanism: CKM_RSA_PKCS_KEY_PAIR_GEN
  Allowed Mechanisms: <Attribute not present>
  Subject (DER, hex): <NULL_PTR>
  Sensitive: true
  Secondary Authentication: <Attribute not present>
  Secondary Authentication PIN Flags: <Attribute not present>
  Decrypt: false
  Sign: true
  Sign Recover: false
  Unwrap: false
  Extractable: false
  Always Sensitive: true
  Never Extractable: true
  Wrap With Trusted: false
  Unwrap Template: <Attribute not present>
  Always Authenticate: false
  Modulus (hex): b35855c1be3ce384921161ac7928f347ded8e8c6c8fa419e92217d0eb1cc49212e3146a74ad4a6035d19fc1156300680c3076d2cd758357fa2ef578c00d635286bbc2909fa88f7c62c5a33c60e1991819f40597c8d7bbe2775f3732fc3e8ece840517d72a6e2c48bdb66dee5a6a6c1855e9a50dccf9c2d9897d0fce38116d702455680ce0215174b1243b0be6dc14b5d7e0e7185c01e02e57bc9f2238913e61eb24bd2100e886472cbbb5a80997b94646293f81cb20866eec893055a771a92512d8d654618896e3840e2f1e8f01b263b521feb92364c76de5084a85b6cd4c1ce25ea6178e48fd8dc4574bf1da5e4988a9142f0baf7887773e03216e600d0a447
  Public Exponent (hex): 010001
  Private Exponent (hex): <Value is sensitive>
  Prime 1 (hex): <Value is sensitive>
  Prime 2 (hex): <Value is sensitive>
  Exponent 1 (hex): <Value is sensitive>
  Exponent 2 (hex): <Value is sensitive>
  Coefficient (hex): <Value is sensitive>
[!] Error extracting private key: 'CKR_KEY_UNEXTRACTABLE'
[*] Extracting RSA Private Key (CKA_EXTRACTABLE: false) with modulus 'AD:DA:B6:30:CC:A7:01:00:55:09:3E:45:11:2A:15:99:67:B9:F7:FF:88:66:67:40:64:FC:9D:40:66:AA:64:51:3F:41:2C:99:92:91:84:75:8F:94:0F:66:6F:92:74:3A:86:57:19:10:DD:28:52:84:EF:1A:E4:BF:39:37:D2:54:29:69:49:0C:0D:38:58:89:E7:C5:61:CC:78:FE:99:09:8B:97:58:B4:9F:44:1B:98:67:1C:5C:C7:06:F5:20:10:D5:E5:2E:5C:5C:9C:7E:17:FE:19:A4:73:28:C0:72:6C:CA:90:36:89:7C:FE:C4:24:8E:C3:C9:FF:DC:66:09:FB:0C:73:9A:2F:98:3A:39:64:4E:E7:9D:68:89:AA:27:09:2E:C0:0A:BB:D3:D8:0B:91:73:EF:32:08:1C:F9:52:03:57:3F:D8:2E:38:08:66:7B:B9:3C:8C:11:09:0E:B5:54:D7:5E:4F:6D:24:BD:26:67:EF:E1:9F:2B:B7:59:EA:7C:8F:CB:92:77:E5:81:54:31:D2:B5:8E:06:A1:CE:CD:C2:0F:EF:20:21:A3:93:63:A2:AA:F1:6C:5D:30:4B:7D:E8:AC:AE:E7:61:5C:EE:2C:9D:56:8D:55:C1:8A:72:3B:DB:51:1E:79:89:12:DB:26:F7:55:0E:FA:D3:AD:00:30:BF'
[?] Key Information:   Object Class: Private Key
  Token: true
  Private: true
  Modifiable: false
  Label: NOEXTRACTABLE_NOWRAP_UNWRAP
  Key Type: RSA
  ID: <NULL_PTR>
  Start Date: <NULL_PTR>
  End Date: <NULL_PTR>
  Derive: false
  Local: true
  Key Generation Mechanism: CKM_RSA_PKCS_KEY_PAIR_GEN
  Allowed Mechanisms: <Attribute not present>
  Subject (DER, hex): <NULL_PTR>
  Sensitive: true
  Secondary Authentication: <Attribute not present>
  Secondary Authentication PIN Flags: <Attribute not present>
  Decrypt: false
  Sign: true
  Sign Recover: false
  Unwrap: true
  Extractable: false
  Always Sensitive: true
  Never Extractable: true
  Wrap With Trusted: false
  Unwrap Template: <Attribute not present>
  Always Authenticate: false
  Modulus (hex): addab630cca7010055093e45112a159967b9f7ff8866674064fc9d4066aa64513f412c99929184758f940f666f92743a86571910dd285284ef1ae4bf3937d2542969490c0d385889e7c561cc78fe99098b9758b49f441b98671c5cc706f52010d5e52e5c5c9c7e17fe19a47328c0726cca9036897cfec4248ec3c9ffdc6609fb0c739a2f983a39644ee79d6889aa27092ec00abbd3d80b9173ef32081cf95203573fd82e3808667bb93c8c11090eb554d75e4f6d24bd2667efe19f2bb759ea7c8fcb9277e5815431d2b58e06a1cecdc20fef2021a39363a2aaf16c5d304b7de8acaee7615cee2c9d568d55c18a723bdb511e798912db26f7550efad3ad0030bf
  Public Exponent (hex): 010001
  Private Exponent (hex): <Value is sensitive>
  Prime 1 (hex): <Value is sensitive>
  Prime 2 (hex): <Value is sensitive>
  Exponent 1 (hex): <Value is sensitive>
  Exponent 2 (hex): <Value is sensitive>
  Coefficient (hex): <Value is sensitive>
[!] Error extracting private key: 'CKR_KEY_UNEXTRACTABLE'
[*] Extracting RSA Private Key (CKA_EXTRACTABLE: false) with modulus 'C1:36:23:CF:DA:57:2F:4F:D0:85:D3:BF:91:5C:FC:CC:3B:7C:D1:9D:F2:05:E5:BA:4A:B2:78:42:CE:91:09:47:8D:39:9C:1F:9B:CC:89:0E:EC:D6:06:AA:41:AC:1D:4B:87:F2:BD:69:BA:39:51:74:B8:8C:0A:EE:7D:DB:45:00:F7:0B:40:21:EE:CE:F8:E2:DC:FC:88:73:63:B8:DC:6D:AC:6E:31:92:2A:BE:E5:13:FB:69:14:CD:58:D4:45:C4:B8:F0:81:22:79:FE:E2:DA:33:26:EA:74:4E:9A:78:6E:92:C6:D4:B8:E7:D2:CF:92:1F:D4:93:8B:A9:EB:1C:0F:61:52:5B:BB:70:6D:6A:C9:FD:AA:6E:DF:4C:02:89:B3:9B:AB:72:C1:26:50:8E:23:6D:C1:F0:A8:16:B6:D3:4B:5A:80:37:73:BF:13:19:E2:72:BF:53:15:17:4A:BB:23:77:1F:05:6B:78:50:74:9C:5A:5A:A9:CA:21:A3:25:E7:31:F6:76:82:A2:28:9C:35:A5:A7:28:35:B9:2A:A5:5F:90:E7:5E:BC:EA:32:ED:0D:20:C1:F3:A7:9D:93:B3:8A:C8:6D:D9:1D:9D:5F:FA:4C:C5:C1:DF:CC:77:BD:3C:8C:D9:69:B3:DB:8E:4B:13:05:25:3F:FC:7F:8F:C7:B0:67'
[?] Key Information:   Object Class: Private Key
  Token: true
  Private: true
  Modifiable: false
  Label: NOEXTRACTABLE_WRAP_NOUNWRAP
  Key Type: RSA
  ID: <NULL_PTR>
  Start Date: <NULL_PTR>
  End Date: <NULL_PTR>
  Derive: false
  Local: true
  Key Generation Mechanism: CKM_RSA_PKCS_KEY_PAIR_GEN
  Allowed Mechanisms: <Attribute not present>
  Subject (DER, hex): <NULL_PTR>
  Sensitive: true
  Secondary Authentication: <Attribute not present>
  Secondary Authentication PIN Flags: <Attribute not present>
  Decrypt: false
  Sign: true
  Sign Recover: false
  Unwrap: false
  Extractable: false
  Always Sensitive: true
  Never Extractable: true
  Wrap With Trusted: false
  Unwrap Template: <Attribute not present>
  Always Authenticate: false
  Modulus (hex): c13623cfda572f4fd085d3bf915cfccc3b7cd19df205e5ba4ab27842ce9109478d399c1f9bcc890eecd606aa41ac1d4b87f2bd69ba395174b88c0aee7ddb4500f70b4021eecef8e2dcfc887363b8dc6dac6e31922abee513fb6914cd58d445c4b8f0812279fee2da3326ea744e9a786e92c6d4b8e7d2cf921fd4938ba9eb1c0f61525bbb706d6ac9fdaa6edf4c0289b39bab72c126508e236dc1f0a816b6d34b5a803773bf1319e272bf5315174abb23771f056b7850749c5a5aa9ca21a325e731f67682a2289c35a5a72835b92aa55f90e75ebcea32ed0d20c1f3a79d93b38ac86dd91d9d5ffa4cc5c1dfcc77bd3c8cd969b3db8e4b1305253ffc7f8fc7b067
  Public Exponent (hex): 010001
  Private Exponent (hex): <Value is sensitive>
  Prime 1 (hex): <Value is sensitive>
  Prime 2 (hex): <Value is sensitive>
  Exponent 1 (hex): <Value is sensitive>
  Exponent 2 (hex): <Value is sensitive>
  Coefficient (hex): <Value is sensitive>
[!] Error extracting private key: 'CKR_KEY_UNEXTRACTABLE'
[*] Extracting RSA Private Key (CKA_EXTRACTABLE: false) with modulus 'C0:3E:60:C5:4C:74:83:D2:CC:D3:F2:F0:59:45:99:6A:7C:9B:F0:4D:56:D1:39:D9:84:C9:89:71:25:D8:0F:60:C3:7D:6D:2F:ED:18:CF:71:8A:DD:55:3C:8F:4F:A0:9B:3C:2A:41:76:51:4E:5B:B5:45:3C:FB:01:B7:9E:13:30:1C:8E:68:EF:39:56:7C:1D:C8:41:6E:C6:F1:0A:7F:94:3D:C0:70:19:E7:AB:23:BC:42:2A:9B:89:C3:7D:73:71:72:A7:77:9D:96:17:81:69:9E:8E:98:BB:80:82:6C:32:7C:F9:6E:5B:50:73:11:D3:50:AB:6B:9B:F5:40:AB:C4:05:2B:18:9A:56:49:23:7C:B4:A1:33:C9:FB:30:9C:78:20:B5:81:61:DD:70:35:A3:5A:80:93:D1:96:EC:7A:75:29:DA:A3:0B:18:F9:B4:62:97:28:BA:6C:1E:F3:EE:4E:18:05:FA:2E:02:61:BB:B8:46:72:7B:65:57:B5:2E:7F:B1:50:64:DB:9A:C1:E5:76:83:A6:74:63:8E:92:B5:7F:B1:D0:D4:59:36:03:2E:8D:9B:D6:2D:0E:60:D3:9C:36:C7:D2:80:FA:7F:51:5B:35:0D:30:F7:71:FD:E3:74:B0:39:5F:98:1A:DA:70:E3:EB:56:B8:92:A0:A6:9F:CD:9D'
[?] Key Information:   Object Class: Private Key
  Token: true
  Private: true
  Modifiable: false
  Label: NOEXTRACTABLE_WRAP_UNWRAP
  Key Type: RSA
  ID: <NULL_PTR>
  Start Date: <NULL_PTR>
  End Date: <NULL_PTR>
  Derive: false
  Local: true
  Key Generation Mechanism: CKM_RSA_PKCS_KEY_PAIR_GEN
  Allowed Mechanisms: <Attribute not present>
  Subject (DER, hex): <NULL_PTR>
  Sensitive: true
  Secondary Authentication: <Attribute not present>
  Secondary Authentication PIN Flags: <Attribute not present>
  Decrypt: false
  Sign: true
  Sign Recover: false
  Unwrap: true
  Extractable: false
  Always Sensitive: true
  Never Extractable: true
  Wrap With Trusted: false
  Unwrap Template: <Attribute not present>
  Always Authenticate: false
  Modulus (hex): c03e60c54c7483d2ccd3f2f05945996a7c9bf04d56d139d984c9897125d80f60c37d6d2fed18cf718add553c8f4fa09b3c2a4176514e5bb5453cfb01b79e13301c8e68ef39567c1dc8416ec6f10a7f943dc07019e7ab23bc422a9b89c37d737172a7779d961781699e8e98bb80826c327cf96e5b507311d350ab6b9bf540abc4052b189a5649237cb4a133c9fb309c7820b58161dd7035a35a8093d196ec7a7529daa30b18f9b4629728ba6c1ef3ee4e1805fa2e0261bbb846727b6557b52e7fb15064db9ac1e57683a674638e92b57fb1d0d45936032e8d9bd62d0e60d39c36c7d280fa7f515b350d30f771fde374b0395f981ada70e3eb56b892a0a69fcd9d
  Public Exponent (hex): 010001
  Private Exponent (hex): <Value is sensitive>
  Prime 1 (hex): <Value is sensitive>
  Prime 2 (hex): <Value is sensitive>
  Exponent 1 (hex): <Value is sensitive>
  Exponent 2 (hex): <Value is sensitive>
  Coefficient (hex): <Value is sensitive>
[!] Error extracting private key: 'CKR_KEY_UNEXTRACTABLE'
[*] Closing session on slot '1'
[*] Unregistering PKCS#11 Module '/usr/lib64/libnethsm.so'
```








