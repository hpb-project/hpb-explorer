package com.hpb.bc.contracts.abi;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.TypeReference.StaticArrayTypeReference;
import io.hpb.web3.abi.datatypes.*;
import io.hpb.web3.abi.datatypes.generated.Bytes1;
import io.hpb.web3.abi.datatypes.generated.Bytes10;
import io.hpb.web3.abi.datatypes.generated.Bytes11;
import io.hpb.web3.abi.datatypes.generated.Bytes12;
import io.hpb.web3.abi.datatypes.generated.Bytes13;
import io.hpb.web3.abi.datatypes.generated.Bytes14;
import io.hpb.web3.abi.datatypes.generated.Bytes15;
import io.hpb.web3.abi.datatypes.generated.Bytes16;
import io.hpb.web3.abi.datatypes.generated.Bytes17;
import io.hpb.web3.abi.datatypes.generated.Bytes18;
import io.hpb.web3.abi.datatypes.generated.Bytes19;
import io.hpb.web3.abi.datatypes.generated.Bytes2;
import io.hpb.web3.abi.datatypes.generated.Bytes20;
import io.hpb.web3.abi.datatypes.generated.Bytes21;
import io.hpb.web3.abi.datatypes.generated.Bytes22;
import io.hpb.web3.abi.datatypes.generated.Bytes23;
import io.hpb.web3.abi.datatypes.generated.Bytes24;
import io.hpb.web3.abi.datatypes.generated.Bytes25;
import io.hpb.web3.abi.datatypes.generated.Bytes26;
import io.hpb.web3.abi.datatypes.generated.Bytes27;
import io.hpb.web3.abi.datatypes.generated.Bytes28;
import io.hpb.web3.abi.datatypes.generated.Bytes29;
import io.hpb.web3.abi.datatypes.generated.Bytes3;
import io.hpb.web3.abi.datatypes.generated.Bytes30;
import io.hpb.web3.abi.datatypes.generated.Bytes31;
import io.hpb.web3.abi.datatypes.generated.Bytes32;
import io.hpb.web3.abi.datatypes.generated.Bytes4;
import io.hpb.web3.abi.datatypes.generated.Bytes5;
import io.hpb.web3.abi.datatypes.generated.Bytes6;
import io.hpb.web3.abi.datatypes.generated.Bytes7;
import io.hpb.web3.abi.datatypes.generated.Bytes8;
import io.hpb.web3.abi.datatypes.generated.Bytes9;
import io.hpb.web3.abi.datatypes.generated.Int104;
import io.hpb.web3.abi.datatypes.generated.Int112;
import io.hpb.web3.abi.datatypes.generated.Int120;
import io.hpb.web3.abi.datatypes.generated.Int128;
import io.hpb.web3.abi.datatypes.generated.Int136;
import io.hpb.web3.abi.datatypes.generated.Int144;
import io.hpb.web3.abi.datatypes.generated.Int152;
import io.hpb.web3.abi.datatypes.generated.Int16;
import io.hpb.web3.abi.datatypes.generated.Int160;
import io.hpb.web3.abi.datatypes.generated.Int168;
import io.hpb.web3.abi.datatypes.generated.Int176;
import io.hpb.web3.abi.datatypes.generated.Int184;
import io.hpb.web3.abi.datatypes.generated.Int192;
import io.hpb.web3.abi.datatypes.generated.Int200;
import io.hpb.web3.abi.datatypes.generated.Int208;
import io.hpb.web3.abi.datatypes.generated.Int216;
import io.hpb.web3.abi.datatypes.generated.Int224;
import io.hpb.web3.abi.datatypes.generated.Int232;
import io.hpb.web3.abi.datatypes.generated.Int24;
import io.hpb.web3.abi.datatypes.generated.Int240;
import io.hpb.web3.abi.datatypes.generated.Int248;
import io.hpb.web3.abi.datatypes.generated.Int256;
import io.hpb.web3.abi.datatypes.generated.Int32;
import io.hpb.web3.abi.datatypes.generated.Int40;
import io.hpb.web3.abi.datatypes.generated.Int48;
import io.hpb.web3.abi.datatypes.generated.Int56;
import io.hpb.web3.abi.datatypes.generated.Int64;
import io.hpb.web3.abi.datatypes.generated.Int72;
import io.hpb.web3.abi.datatypes.generated.Int8;
import io.hpb.web3.abi.datatypes.generated.Int80;
import io.hpb.web3.abi.datatypes.generated.Int88;
import io.hpb.web3.abi.datatypes.generated.Int96;
import io.hpb.web3.abi.datatypes.generated.Uint104;
import io.hpb.web3.abi.datatypes.generated.Uint112;
import io.hpb.web3.abi.datatypes.generated.Uint120;
import io.hpb.web3.abi.datatypes.generated.Uint128;
import io.hpb.web3.abi.datatypes.generated.Uint136;
import io.hpb.web3.abi.datatypes.generated.Uint144;
import io.hpb.web3.abi.datatypes.generated.Uint152;
import io.hpb.web3.abi.datatypes.generated.Uint16;
import io.hpb.web3.abi.datatypes.generated.Uint160;
import io.hpb.web3.abi.datatypes.generated.Uint168;
import io.hpb.web3.abi.datatypes.generated.Uint176;
import io.hpb.web3.abi.datatypes.generated.Uint184;
import io.hpb.web3.abi.datatypes.generated.Uint192;
import io.hpb.web3.abi.datatypes.generated.Uint200;
import io.hpb.web3.abi.datatypes.generated.Uint208;
import io.hpb.web3.abi.datatypes.generated.Uint216;
import io.hpb.web3.abi.datatypes.generated.Uint224;
import io.hpb.web3.abi.datatypes.generated.Uint232;
import io.hpb.web3.abi.datatypes.generated.Uint24;
import io.hpb.web3.abi.datatypes.generated.Uint240;
import io.hpb.web3.abi.datatypes.generated.Uint248;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.abi.datatypes.generated.Uint32;
import io.hpb.web3.abi.datatypes.generated.Uint40;
import io.hpb.web3.abi.datatypes.generated.Uint48;
import io.hpb.web3.abi.datatypes.generated.Uint56;
import io.hpb.web3.abi.datatypes.generated.Uint64;
import io.hpb.web3.abi.datatypes.generated.Uint72;
import io.hpb.web3.abi.datatypes.generated.Uint8;
import io.hpb.web3.abi.datatypes.generated.Uint80;
import io.hpb.web3.abi.datatypes.generated.Uint88;
import io.hpb.web3.abi.datatypes.generated.Uint96;

public class AllReferenceType {
    public AllReferenceType() {
    }

    public static TypeReference getStaticType(String type, int size, boolean isIndexed) {
        byte var4 = -1;
        switch(type.hashCode()) {
            case -1374008026:
                if (type.equals("bytes1")) {
                    var4 = 68;
                }
                break;
            case -1374008025:
                if (type.equals("bytes2")) {
                    var4 = 69;
                }
                break;
            case -1374008024:
                if (type.equals("bytes3")) {
                    var4 = 70;
                }
                break;
            case -1374008023:
                if (type.equals("bytes4")) {
                    var4 = 71;
                }
                break;
            case -1374008022:
                if (type.equals("bytes5")) {
                    var4 = 72;
                }
                break;
            case -1374008021:
                if (type.equals("bytes6")) {
                    var4 = 73;
                }
                break;
            case -1374008020:
                if (type.equals("bytes7")) {
                    var4 = 74;
                }
                break;
            case -1374008019:
                if (type.equals("bytes8")) {
                    var4 = 75;
                }
                break;
            case -1374008018:
                if (type.equals("bytes9")) {
                    var4 = 76;
                }
                break;
            case -1183814746:
                if (type.equals("int104")) {
                    var4 = 29;
                }
                break;
            case -1183814717:
                if (type.equals("int112")) {
                    var4 = 31;
                }
                break;
            case -1183814688:
                if (type.equals("int120")) {
                    var4 = 33;
                }
                break;
            case -1183814680:
                if (type.equals("int128")) {
                    var4 = 35;
                }
                break;
            case -1183814651:
                if (type.equals("int136")) {
                    var4 = 37;
                }
                break;
            case -1183814622:
                if (type.equals("int144")) {
                    var4 = 39;
                }
                break;
            case -1183814593:
                if (type.equals("int152")) {
                    var4 = 41;
                }
                break;
            case -1183814564:
                if (type.equals("int160")) {
                    var4 = 43;
                }
                break;
            case -1183814556:
                if (type.equals("int168")) {
                    var4 = 45;
                }
                break;
            case -1183814527:
                if (type.equals("int176")) {
                    var4 = 47;
                }
                break;
            case -1183814498:
                if (type.equals("int184")) {
                    var4 = 49;
                }
                break;
            case -1183814469:
                if (type.equals("int192")) {
                    var4 = 51;
                }
                break;
            case -1183813789:
                if (type.equals("int200")) {
                    var4 = 53;
                }
                break;
            case -1183813781:
                if (type.equals("int208")) {
                    var4 = 55;
                }
                break;
            case -1183813752:
                if (type.equals("int216")) {
                    var4 = 57;
                }
                break;
            case -1183813723:
                if (type.equals("int224")) {
                    var4 = 59;
                }
                break;
            case -1183813694:
                if (type.equals("int232")) {
                    var4 = 61;
                }
                break;
            case -1183813665:
                if (type.equals("int240")) {
                    var4 = 63;
                }
                break;
            case -1183813657:
                if (type.equals("int248")) {
                    var4 = 65;
                }
                break;
            case -1183813628:
                if (type.equals("int256")) {
                    var4 = 67;
                }
                break;
            case -1147692044:
                if (type.equals("address")) {
                    var4 = 0;
                }
                break;
            case -891985903:
                if (type.equals("string")) {
                    var4 = 2;
                }
                break;
            case -844996865:
                if (type.equals("uint16")) {
                    var4 = 6;
                }
                break;
            case -844996836:
                if (type.equals("uint24")) {
                    var4 = 8;
                }
                break;
            case -844996807:
                if (type.equals("uint32")) {
                    var4 = 10;
                }
                break;
            case -844996778:
                if (type.equals("uint40")) {
                    var4 = 12;
                }
                break;
            case -844996770:
                if (type.equals("uint48")) {
                    var4 = 14;
                }
                break;
            case -844996741:
                if (type.equals("uint56")) {
                    var4 = 16;
                }
                break;
            case -844996712:
                if (type.equals("uint64")) {
                    var4 = 18;
                }
                break;
            case -844996683:
                if (type.equals("uint72")) {
                    var4 = 20;
                }
                break;
            case -844996654:
                if (type.equals("uint80")) {
                    var4 = 22;
                }
                break;
            case -844996646:
                if (type.equals("uint88")) {
                    var4 = 24;
                }
                break;
            case -844996617:
                if (type.equals("uint96")) {
                    var4 = 26;
                }
                break;
            case -425099173:
                if (type.equals("uint104")) {
                    var4 = 28;
                }
                break;
            case -425099144:
                if (type.equals("uint112")) {
                    var4 = 30;
                }
                break;
            case -425099115:
                if (type.equals("uint120")) {
                    var4 = 32;
                }
                break;
            case -425099107:
                if (type.equals("uint128")) {
                    var4 = 34;
                }
                break;
            case -425099078:
                if (type.equals("uint136")) {
                    var4 = 36;
                }
                break;
            case -425099049:
                if (type.equals("uint144")) {
                    var4 = 38;
                }
                break;
            case -425099020:
                if (type.equals("uint152")) {
                    var4 = 40;
                }
                break;
            case -425098991:
                if (type.equals("uint160")) {
                    var4 = 42;
                }
                break;
            case -425098983:
                if (type.equals("uint168")) {
                    var4 = 44;
                }
                break;
            case -425098954:
                if (type.equals("uint176")) {
                    var4 = 46;
                }
                break;
            case -425098925:
                if (type.equals("uint184")) {
                    var4 = 48;
                }
                break;
            case -425098896:
                if (type.equals("uint192")) {
                    var4 = 50;
                }
                break;
            case -425098216:
                if (type.equals("uint200")) {
                    var4 = 52;
                }
                break;
            case -425098208:
                if (type.equals("uint208")) {
                    var4 = 54;
                }
                break;
            case -425098179:
                if (type.equals("uint216")) {
                    var4 = 56;
                }
                break;
            case -425098150:
                if (type.equals("uint224")) {
                    var4 = 58;
                }
                break;
            case -425098121:
                if (type.equals("uint232")) {
                    var4 = 60;
                }
                break;
            case -425098092:
                if (type.equals("uint240")) {
                    var4 = 62;
                }
                break;
            case -425098084:
                if (type.equals("uint248")) {
                    var4 = 64;
                }
                break;
            case -425098055:
                if (type.equals("uint256")) {
                    var4 = 66;
                }
                break;
            case 3029738:
                if (type.equals("bool")) {
                    var4 = 1;
                }
                break;
            case 3237417:
                if (type.equals("int8")) {
                    var4 = 5;
                }
                break;
            case 94224491:
                if (type.equals("bytes")) {
                    var4 = 3;
                }
                break;
            case 100359764:
                if (type.equals("int16")) {
                    var4 = 7;
                }
                break;
            case 100359793:
                if (type.equals("int24")) {
                    var4 = 9;
                }
                break;
            case 100359822:
                if (type.equals("int32")) {
                    var4 = 11;
                }
                break;
            case 100359851:
                if (type.equals("int40")) {
                    var4 = 13;
                }
                break;
            case 100359859:
                if (type.equals("int48")) {
                    var4 = 15;
                }
                break;
            case 100359888:
                if (type.equals("int56")) {
                    var4 = 17;
                }
                break;
            case 100359917:
                if (type.equals("int64")) {
                    var4 = 19;
                }
                break;
            case 100359946:
                if (type.equals("int72")) {
                    var4 = 21;
                }
                break;
            case 100359975:
                if (type.equals("int80")) {
                    var4 = 23;
                }
                break;
            case 100359983:
                if (type.equals("int88")) {
                    var4 = 25;
                }
                break;
            case 100360012:
                if (type.equals("int96")) {
                    var4 = 27;
                }
                break;
            case 111289374:
                if (type.equals("uint8")) {
                    var4 = 4;
                }
                break;
            case 355424202:
                if (type.equals("bytes10")) {
                    var4 = 77;
                }
                break;
            case 355424203:
                if (type.equals("bytes11")) {
                    var4 = 78;
                }
                break;
            case 355424204:
                if (type.equals("bytes12")) {
                    var4 = 79;
                }
                break;
            case 355424205:
                if (type.equals("bytes13")) {
                    var4 = 80;
                }
                break;
            case 355424206:
                if (type.equals("bytes14")) {
                    var4 = 81;
                }
                break;
            case 355424207:
                if (type.equals("bytes15")) {
                    var4 = 82;
                }
                break;
            case 355424208:
                if (type.equals("bytes16")) {
                    var4 = 83;
                }
                break;
            case 355424209:
                if (type.equals("bytes17")) {
                    var4 = 84;
                }
                break;
            case 355424210:
                if (type.equals("bytes18")) {
                    var4 = 85;
                }
                break;
            case 355424211:
                if (type.equals("bytes19")) {
                    var4 = 86;
                }
                break;
            case 355424233:
                if (type.equals("bytes20")) {
                    var4 = 87;
                }
                break;
            case 355424234:
                if (type.equals("bytes21")) {
                    var4 = 88;
                }
                break;
            case 355424235:
                if (type.equals("bytes22")) {
                    var4 = 89;
                }
                break;
            case 355424236:
                if (type.equals("bytes23")) {
                    var4 = 90;
                }
                break;
            case 355424237:
                if (type.equals("bytes24")) {
                    var4 = 91;
                }
                break;
            case 355424238:
                if (type.equals("bytes25")) {
                    var4 = 92;
                }
                break;
            case 355424239:
                if (type.equals("bytes26")) {
                    var4 = 93;
                }
                break;
            case 355424240:
                if (type.equals("bytes27")) {
                    var4 = 94;
                }
                break;
            case 355424241:
                if (type.equals("bytes28")) {
                    var4 = 95;
                }
                break;
            case 355424242:
                if (type.equals("bytes29")) {
                    var4 = 96;
                }
                break;
            case 355424264:
                if (type.equals("bytes30")) {
                    var4 = 97;
                }
                break;
            case 355424265:
                if (type.equals("bytes31")) {
                    var4 = 98;
                }
                break;
            case 355424266:
                if (type.equals("bytes32")) {
                    var4 = 99;
                }
        }

        switch(var4) {
            case 0:
                return new StaticArrayTypeReference<StaticArray<Address>>(size) {
                };
            case 1:
                return new StaticArrayTypeReference<StaticArray<Bool>>(size) {
                };
            case 2:
                return new StaticArrayTypeReference<StaticArray<Utf8String>>(size) {
                };
            case 3:
                return new StaticArrayTypeReference<StaticArray<DynamicBytes>>(size) {
                };
            case 4:
                return new StaticArrayTypeReference<StaticArray<Uint8>>(size) {
                };
            case 5:
                return new StaticArrayTypeReference<StaticArray<Int8>>(size) {
                };
            case 6:
                return new StaticArrayTypeReference<StaticArray<Uint16>>(size) {
                };
            case 7:
                return new StaticArrayTypeReference<StaticArray<Int16>>(size) {
                };
            case 8:
                return new StaticArrayTypeReference<StaticArray<Uint24>>(size) {
                };
            case 9:
                return new StaticArrayTypeReference<StaticArray<Int24>>(size) {
                };
            case 10:
                return new StaticArrayTypeReference<StaticArray<Uint32>>(size) {
                };
            case 11:
                return new StaticArrayTypeReference<StaticArray<Int32>>(size) {
                };
            case 12:
                return new StaticArrayTypeReference<StaticArray<Uint40>>(size) {
                };
            case 13:
                return new StaticArrayTypeReference<StaticArray<Int40>>(size) {
                };
            case 14:
                return new StaticArrayTypeReference<StaticArray<Uint48>>(size) {
                };
            case 15:
                return new StaticArrayTypeReference<StaticArray<Int48>>(size) {
                };
            case 16:
                return new StaticArrayTypeReference<StaticArray<Uint56>>(size) {
                };
            case 17:
                return new StaticArrayTypeReference<StaticArray<Int56>>(size) {
                };
            case 18:
                return new StaticArrayTypeReference<StaticArray<Uint64>>(size) {
                };
            case 19:
                return new StaticArrayTypeReference<StaticArray<Int64>>(size) {
                };
            case 20:
                return new StaticArrayTypeReference<StaticArray<Uint72>>(size) {
                };
            case 21:
                return new StaticArrayTypeReference<StaticArray<Int72>>(size) {
                };
            case 22:
                return new StaticArrayTypeReference<StaticArray<Uint80>>(size) {
                };
            case 23:
                return new StaticArrayTypeReference<StaticArray<Int80>>(size) {
                };
            case 24:
                return new StaticArrayTypeReference<StaticArray<Uint88>>(size) {
                };
            case 25:
                return new StaticArrayTypeReference<StaticArray<Int88>>(size) {
                };
            case 26:
                return new StaticArrayTypeReference<StaticArray<Uint96>>(size) {
                };
            case 27:
                return new StaticArrayTypeReference<StaticArray<Int96>>(size) {
                };
            case 28:
                return new StaticArrayTypeReference<StaticArray<Uint104>>(size) {
                };
            case 29:
                return new StaticArrayTypeReference<StaticArray<Int104>>(size) {
                };
            case 30:
                return new StaticArrayTypeReference<StaticArray<Uint112>>(size) {
                };
            case 31:
                return new StaticArrayTypeReference<StaticArray<Int112>>(size) {
                };
            case 32:
                return new StaticArrayTypeReference<StaticArray<Uint120>>(size) {
                };
            case 33:
                return new StaticArrayTypeReference<StaticArray<Int120>>(size) {
                };
            case 34:
                return new StaticArrayTypeReference<StaticArray<Uint128>>(size) {
                };
            case 35:
                return new StaticArrayTypeReference<StaticArray<Int128>>(size) {
                };
            case 36:
                return new StaticArrayTypeReference<StaticArray<Uint136>>(size) {
                };
            case 37:
                return new StaticArrayTypeReference<StaticArray<Int136>>(size) {
                };
            case 38:
                return new StaticArrayTypeReference<StaticArray<Uint144>>(size) {
                };
            case 39:
                return new StaticArrayTypeReference<StaticArray<Int144>>(size) {
                };
            case 40:
                return new StaticArrayTypeReference<StaticArray<Uint152>>(size) {
                };
            case 41:
                return new StaticArrayTypeReference<StaticArray<Int152>>(size) {
                };
            case 42:
                return new StaticArrayTypeReference<StaticArray<Uint160>>(size) {
                };
            case 43:
                return new StaticArrayTypeReference<StaticArray<Int160>>(size) {
                };
            case 44:
                return new StaticArrayTypeReference<StaticArray<Uint168>>(size) {
                };
            case 45:
                return new StaticArrayTypeReference<StaticArray<Int168>>(size) {
                };
            case 46:
                return new StaticArrayTypeReference<StaticArray<Uint176>>(size) {
                };
            case 47:
                return new StaticArrayTypeReference<StaticArray<Int176>>(size) {
                };
            case 48:
                return new StaticArrayTypeReference<StaticArray<Uint184>>(size) {
                };
            case 49:
                return new StaticArrayTypeReference<StaticArray<Int184>>(size) {
                };
            case 50:
                return new StaticArrayTypeReference<StaticArray<Uint192>>(size) {
                };
            case 51:
                return new StaticArrayTypeReference<StaticArray<Int192>>(size) {
                };
            case 52:
                return new StaticArrayTypeReference<StaticArray<Uint200>>(size) {
                };
            case 53:
                return new StaticArrayTypeReference<StaticArray<Int200>>(size) {
                };
            case 54:
                return new StaticArrayTypeReference<StaticArray<Uint208>>(size) {
                };
            case 55:
                return new StaticArrayTypeReference<StaticArray<Int208>>(size) {
                };
            case 56:
                return new StaticArrayTypeReference<StaticArray<Uint216>>(size) {
                };
            case 57:
                return new StaticArrayTypeReference<StaticArray<Int216>>(size) {
                };
            case 58:
                return new StaticArrayTypeReference<StaticArray<Uint224>>(size) {
                };
            case 59:
                return new StaticArrayTypeReference<StaticArray<Int224>>(size) {
                };
            case 60:
                return new StaticArrayTypeReference<StaticArray<Uint232>>(size) {
                };
            case 61:
                return new StaticArrayTypeReference<StaticArray<Int232>>(size) {
                };
            case 62:
                return new StaticArrayTypeReference<StaticArray<Uint240>>(size) {
                };
            case 63:
                return new StaticArrayTypeReference<StaticArray<Int240>>(size) {
                };
            case 64:
                return new StaticArrayTypeReference<StaticArray<Uint248>>(size) {
                };
            case 65:
                return new StaticArrayTypeReference<StaticArray<Int248>>(size) {
                };
            case 66:
                return new StaticArrayTypeReference<StaticArray<Uint256>>(size) {
                };
            case 67:
                return new StaticArrayTypeReference<StaticArray<Int256>>(size) {
                };
            case 68:
                return new StaticArrayTypeReference<StaticArray<Bytes1>>(size) {
                };
            case 69:
                return new StaticArrayTypeReference<StaticArray<Bytes2>>(size) {
                };
            case 70:
                return new StaticArrayTypeReference<StaticArray<Bytes3>>(size) {
                };
            case 71:
                return new StaticArrayTypeReference<StaticArray<Bytes4>>(size) {
                };
            case 72:
                return new StaticArrayTypeReference<StaticArray<Bytes5>>(size) {
                };
            case 73:
                return new StaticArrayTypeReference<StaticArray<Bytes6>>(size) {
                };
            case 74:
                return new StaticArrayTypeReference<StaticArray<Bytes7>>(size) {
                };
            case 75:
                return new StaticArrayTypeReference<StaticArray<Bytes8>>(size) {
                };
            case 76:
                return new StaticArrayTypeReference<StaticArray<Bytes9>>(size) {
                };
            case 77:
                return new StaticArrayTypeReference<StaticArray<Bytes10>>(size) {
                };
            case 78:
                return new StaticArrayTypeReference<StaticArray<Bytes11>>(size) {
                };
            case 79:
                return new StaticArrayTypeReference<StaticArray<Bytes12>>(size) {
                };
            case 80:
                return new StaticArrayTypeReference<StaticArray<Bytes13>>(size) {
                };
            case 81:
                return new StaticArrayTypeReference<StaticArray<Bytes14>>(size) {
                };
            case 82:
                return new StaticArrayTypeReference<StaticArray<Bytes15>>(size) {
                };
            case 83:
                return new StaticArrayTypeReference<StaticArray<Bytes16>>(size) {
                };
            case 84:
                return new StaticArrayTypeReference<StaticArray<Bytes17>>(size) {
                };
            case 85:
                return new StaticArrayTypeReference<StaticArray<Bytes18>>(size) {
                };
            case 86:
                return new StaticArrayTypeReference<StaticArray<Bytes19>>(size) {
                };
            case 87:
                return new StaticArrayTypeReference<StaticArray<Bytes20>>(size) {
                };
            case 88:
                return new StaticArrayTypeReference<StaticArray<Bytes21>>(size) {
                };
            case 89:
                return new StaticArrayTypeReference<StaticArray<Bytes22>>(size) {
                };
            case 90:
                return new StaticArrayTypeReference<StaticArray<Bytes23>>(size) {
                };
            case 91:
                return new StaticArrayTypeReference<StaticArray<Bytes24>>(size) {
                };
            case 92:
                return new StaticArrayTypeReference<StaticArray<Bytes25>>(size) {
                };
            case 93:
                return new StaticArrayTypeReference<StaticArray<Bytes26>>(size) {
                };
            case 94:
                return new StaticArrayTypeReference<StaticArray<Bytes27>>(size) {
                };
            case 95:
                return new StaticArrayTypeReference<StaticArray<Bytes28>>(size) {
                };
            case 96:
                return new StaticArrayTypeReference<StaticArray<Bytes29>>(size) {
                };
            case 97:
                return new StaticArrayTypeReference<StaticArray<Bytes30>>(size) {
                };
            case 98:
                return new StaticArrayTypeReference<StaticArray<Bytes31>>(size) {
                };
            case 99:
                return new StaticArrayTypeReference<StaticArray<Bytes32>>(size) {
                };
            default:
                throw new UnsupportedOperationException("Unsupported type encountered: " + type);
        }
    }

    public static TypeReference getDynamicType(String type, boolean isIndexed) {
        byte var3 = -1;
        switch(type.hashCode()) {
            case -1374008026:
                if (type.equals("bytes1")) {
                    var3 = 68;
                }
                break;
            case -1374008025:
                if (type.equals("bytes2")) {
                    var3 = 69;
                }
                break;
            case -1374008024:
                if (type.equals("bytes3")) {
                    var3 = 70;
                }
                break;
            case -1374008023:
                if (type.equals("bytes4")) {
                    var3 = 71;
                }
                break;
            case -1374008022:
                if (type.equals("bytes5")) {
                    var3 = 72;
                }
                break;
            case -1374008021:
                if (type.equals("bytes6")) {
                    var3 = 73;
                }
                break;
            case -1374008020:
                if (type.equals("bytes7")) {
                    var3 = 74;
                }
                break;
            case -1374008019:
                if (type.equals("bytes8")) {
                    var3 = 75;
                }
                break;
            case -1374008018:
                if (type.equals("bytes9")) {
                    var3 = 76;
                }
                break;
            case -1183814746:
                if (type.equals("int104")) {
                    var3 = 29;
                }
                break;
            case -1183814717:
                if (type.equals("int112")) {
                    var3 = 31;
                }
                break;
            case -1183814688:
                if (type.equals("int120")) {
                    var3 = 33;
                }
                break;
            case -1183814680:
                if (type.equals("int128")) {
                    var3 = 35;
                }
                break;
            case -1183814651:
                if (type.equals("int136")) {
                    var3 = 37;
                }
                break;
            case -1183814622:
                if (type.equals("int144")) {
                    var3 = 39;
                }
                break;
            case -1183814593:
                if (type.equals("int152")) {
                    var3 = 41;
                }
                break;
            case -1183814564:
                if (type.equals("int160")) {
                    var3 = 43;
                }
                break;
            case -1183814556:
                if (type.equals("int168")) {
                    var3 = 45;
                }
                break;
            case -1183814527:
                if (type.equals("int176")) {
                    var3 = 47;
                }
                break;
            case -1183814498:
                if (type.equals("int184")) {
                    var3 = 49;
                }
                break;
            case -1183814469:
                if (type.equals("int192")) {
                    var3 = 51;
                }
                break;
            case -1183813789:
                if (type.equals("int200")) {
                    var3 = 53;
                }
                break;
            case -1183813781:
                if (type.equals("int208")) {
                    var3 = 55;
                }
                break;
            case -1183813752:
                if (type.equals("int216")) {
                    var3 = 57;
                }
                break;
            case -1183813723:
                if (type.equals("int224")) {
                    var3 = 59;
                }
                break;
            case -1183813694:
                if (type.equals("int232")) {
                    var3 = 61;
                }
                break;
            case -1183813665:
                if (type.equals("int240")) {
                    var3 = 63;
                }
                break;
            case -1183813657:
                if (type.equals("int248")) {
                    var3 = 65;
                }
                break;
            case -1183813628:
                if (type.equals("int256")) {
                    var3 = 67;
                }
                break;
            case -1147692044:
                if (type.equals("address")) {
                    var3 = 0;
                }
                break;
            case -891985903:
                if (type.equals("string")) {
                    var3 = 2;
                }
                break;
            case -844996865:
                if (type.equals("uint16")) {
                    var3 = 6;
                }
                break;
            case -844996836:
                if (type.equals("uint24")) {
                    var3 = 8;
                }
                break;
            case -844996807:
                if (type.equals("uint32")) {
                    var3 = 10;
                }
                break;
            case -844996778:
                if (type.equals("uint40")) {
                    var3 = 12;
                }
                break;
            case -844996770:
                if (type.equals("uint48")) {
                    var3 = 14;
                }
                break;
            case -844996741:
                if (type.equals("uint56")) {
                    var3 = 16;
                }
                break;
            case -844996712:
                if (type.equals("uint64")) {
                    var3 = 18;
                }
                break;
            case -844996683:
                if (type.equals("uint72")) {
                    var3 = 20;
                }
                break;
            case -844996654:
                if (type.equals("uint80")) {
                    var3 = 22;
                }
                break;
            case -844996646:
                if (type.equals("uint88")) {
                    var3 = 24;
                }
                break;
            case -844996617:
                if (type.equals("uint96")) {
                    var3 = 26;
                }
                break;
            case -425099173:
                if (type.equals("uint104")) {
                    var3 = 28;
                }
                break;
            case -425099144:
                if (type.equals("uint112")) {
                    var3 = 30;
                }
                break;
            case -425099115:
                if (type.equals("uint120")) {
                    var3 = 32;
                }
                break;
            case -425099107:
                if (type.equals("uint128")) {
                    var3 = 34;
                }
                break;
            case -425099078:
                if (type.equals("uint136")) {
                    var3 = 36;
                }
                break;
            case -425099049:
                if (type.equals("uint144")) {
                    var3 = 38;
                }
                break;
            case -425099020:
                if (type.equals("uint152")) {
                    var3 = 40;
                }
                break;
            case -425098991:
                if (type.equals("uint160")) {
                    var3 = 42;
                }
                break;
            case -425098983:
                if (type.equals("uint168")) {
                    var3 = 44;
                }
                break;
            case -425098954:
                if (type.equals("uint176")) {
                    var3 = 46;
                }
                break;
            case -425098925:
                if (type.equals("uint184")) {
                    var3 = 48;
                }
                break;
            case -425098896:
                if (type.equals("uint192")) {
                    var3 = 50;
                }
                break;
            case -425098216:
                if (type.equals("uint200")) {
                    var3 = 52;
                }
                break;
            case -425098208:
                if (type.equals("uint208")) {
                    var3 = 54;
                }
                break;
            case -425098179:
                if (type.equals("uint216")) {
                    var3 = 56;
                }
                break;
            case -425098150:
                if (type.equals("uint224")) {
                    var3 = 58;
                }
                break;
            case -425098121:
                if (type.equals("uint232")) {
                    var3 = 60;
                }
                break;
            case -425098092:
                if (type.equals("uint240")) {
                    var3 = 62;
                }
                break;
            case -425098084:
                if (type.equals("uint248")) {
                    var3 = 64;
                }
                break;
            case -425098055:
                if (type.equals("uint256")) {
                    var3 = 66;
                }
                break;
            case 3029738:
                if (type.equals("bool")) {
                    var3 = 1;
                }
                break;
            case 3237417:
                if (type.equals("int8")) {
                    var3 = 5;
                }
                break;
            case 94224491:
                if (type.equals("bytes")) {
                    var3 = 3;
                }
                break;
            case 100359764:
                if (type.equals("int16")) {
                    var3 = 7;
                }
                break;
            case 100359793:
                if (type.equals("int24")) {
                    var3 = 9;
                }
                break;
            case 100359822:
                if (type.equals("int32")) {
                    var3 = 11;
                }
                break;
            case 100359851:
                if (type.equals("int40")) {
                    var3 = 13;
                }
                break;
            case 100359859:
                if (type.equals("int48")) {
                    var3 = 15;
                }
                break;
            case 100359888:
                if (type.equals("int56")) {
                    var3 = 17;
                }
                break;
            case 100359917:
                if (type.equals("int64")) {
                    var3 = 19;
                }
                break;
            case 100359946:
                if (type.equals("int72")) {
                    var3 = 21;
                }
                break;
            case 100359975:
                if (type.equals("int80")) {
                    var3 = 23;
                }
                break;
            case 100359983:
                if (type.equals("int88")) {
                    var3 = 25;
                }
                break;
            case 100360012:
                if (type.equals("int96")) {
                    var3 = 27;
                }
                break;
            case 111289374:
                if (type.equals("uint8")) {
                    var3 = 4;
                }
                break;
            case 355424202:
                if (type.equals("bytes10")) {
                    var3 = 77;
                }
                break;
            case 355424203:
                if (type.equals("bytes11")) {
                    var3 = 78;
                }
                break;
            case 355424204:
                if (type.equals("bytes12")) {
                    var3 = 79;
                }
                break;
            case 355424205:
                if (type.equals("bytes13")) {
                    var3 = 80;
                }
                break;
            case 355424206:
                if (type.equals("bytes14")) {
                    var3 = 81;
                }
                break;
            case 355424207:
                if (type.equals("bytes15")) {
                    var3 = 82;
                }
                break;
            case 355424208:
                if (type.equals("bytes16")) {
                    var3 = 83;
                }
                break;
            case 355424209:
                if (type.equals("bytes17")) {
                    var3 = 84;
                }
                break;
            case 355424210:
                if (type.equals("bytes18")) {
                    var3 = 85;
                }
                break;
            case 355424211:
                if (type.equals("bytes19")) {
                    var3 = 86;
                }
                break;
            case 355424233:
                if (type.equals("bytes20")) {
                    var3 = 87;
                }
                break;
            case 355424234:
                if (type.equals("bytes21")) {
                    var3 = 88;
                }
                break;
            case 355424235:
                if (type.equals("bytes22")) {
                    var3 = 89;
                }
                break;
            case 355424236:
                if (type.equals("bytes23")) {
                    var3 = 90;
                }
                break;
            case 355424237:
                if (type.equals("bytes24")) {
                    var3 = 91;
                }
                break;
            case 355424238:
                if (type.equals("bytes25")) {
                    var3 = 92;
                }
                break;
            case 355424239:
                if (type.equals("bytes26")) {
                    var3 = 93;
                }
                break;
            case 355424240:
                if (type.equals("bytes27")) {
                    var3 = 94;
                }
                break;
            case 355424241:
                if (type.equals("bytes28")) {
                    var3 = 95;
                }
                break;
            case 355424242:
                if (type.equals("bytes29")) {
                    var3 = 96;
                }
                break;
            case 355424264:
                if (type.equals("bytes30")) {
                    var3 = 97;
                }
                break;
            case 355424265:
                if (type.equals("bytes31")) {
                    var3 = 98;
                }
                break;
            case 355424266:
                if (type.equals("bytes32")) {
                    var3 = 99;
                }
        }

        switch(var3) {
            case 0:
                return new TypeReference<DynamicArray<Address>>(isIndexed) {
                };
            case 1:
                return new TypeReference<DynamicArray<Bool>>(isIndexed) {
                };
            case 2:
                return new TypeReference<DynamicArray<Utf8String>>(isIndexed) {
                };
            case 3:
                return new TypeReference<DynamicArray<DynamicBytes>>(isIndexed) {
                };
            case 4:
                return new TypeReference<DynamicArray<Uint8>>(isIndexed) {
                };
            case 5:
                return new TypeReference<DynamicArray<Int8>>(isIndexed) {
                };
            case 6:
                return new TypeReference<DynamicArray<Uint16>>(isIndexed) {
                };
            case 7:
                return new TypeReference<DynamicArray<Int16>>(isIndexed) {
                };
            case 8:
                return new TypeReference<DynamicArray<Uint24>>(isIndexed) {
                };
            case 9:
                return new TypeReference<DynamicArray<Int24>>(isIndexed) {
                };
            case 10:
                return new TypeReference<DynamicArray<Uint32>>(isIndexed) {
                };
            case 11:
                return new TypeReference<DynamicArray<Int32>>(isIndexed) {
                };
            case 12:
                return new TypeReference<DynamicArray<Uint40>>(isIndexed) {
                };
            case 13:
                return new TypeReference<DynamicArray<Int40>>(isIndexed) {
                };
            case 14:
                return new TypeReference<DynamicArray<Uint48>>(isIndexed) {
                };
            case 15:
                return new TypeReference<DynamicArray<Int48>>(isIndexed) {
                };
            case 16:
                return new TypeReference<DynamicArray<Uint56>>(isIndexed) {
                };
            case 17:
                return new TypeReference<DynamicArray<Int56>>(isIndexed) {
                };
            case 18:
                return new TypeReference<DynamicArray<Uint64>>(isIndexed) {
                };
            case 19:
                return new TypeReference<DynamicArray<Int64>>(isIndexed) {
                };
            case 20:
                return new TypeReference<DynamicArray<Uint72>>(isIndexed) {
                };
            case 21:
                return new TypeReference<DynamicArray<Int72>>(isIndexed) {
                };
            case 22:
                return new TypeReference<DynamicArray<Uint80>>(isIndexed) {
                };
            case 23:
                return new TypeReference<DynamicArray<Int80>>(isIndexed) {
                };
            case 24:
                return new TypeReference<DynamicArray<Uint88>>(isIndexed) {
                };
            case 25:
                return new TypeReference<DynamicArray<Int88>>(isIndexed) {
                };
            case 26:
                return new TypeReference<DynamicArray<Uint96>>(isIndexed) {
                };
            case 27:
                return new TypeReference<DynamicArray<Int96>>(isIndexed) {
                };
            case 28:
                return new TypeReference<DynamicArray<Uint104>>(isIndexed) {
                };
            case 29:
                return new TypeReference<DynamicArray<Int104>>(isIndexed) {
                };
            case 30:
                return new TypeReference<DynamicArray<Uint112>>(isIndexed) {
                };
            case 31:
                return new TypeReference<DynamicArray<Int112>>(isIndexed) {
                };
            case 32:
                return new TypeReference<DynamicArray<Uint120>>(isIndexed) {
                };
            case 33:
                return new TypeReference<DynamicArray<Int120>>(isIndexed) {
                };
            case 34:
                return new TypeReference<DynamicArray<Uint128>>(isIndexed) {
                };
            case 35:
                return new TypeReference<DynamicArray<Int128>>(isIndexed) {
                };
            case 36:
                return new TypeReference<DynamicArray<Uint136>>(isIndexed) {
                };
            case 37:
                return new TypeReference<DynamicArray<Int136>>(isIndexed) {
                };
            case 38:
                return new TypeReference<DynamicArray<Uint144>>(isIndexed) {
                };
            case 39:
                return new TypeReference<DynamicArray<Int144>>(isIndexed) {
                };
            case 40:
                return new TypeReference<DynamicArray<Uint152>>(isIndexed) {
                };
            case 41:
                return new TypeReference<DynamicArray<Int152>>(isIndexed) {
                };
            case 42:
                return new TypeReference<DynamicArray<Uint160>>(isIndexed) {
                };
            case 43:
                return new TypeReference<DynamicArray<Int160>>(isIndexed) {
                };
            case 44:
                return new TypeReference<DynamicArray<Uint168>>(isIndexed) {
                };
            case 45:
                return new TypeReference<DynamicArray<Int168>>(isIndexed) {
                };
            case 46:
                return new TypeReference<DynamicArray<Uint176>>(isIndexed) {
                };
            case 47:
                return new TypeReference<DynamicArray<Int176>>(isIndexed) {
                };
            case 48:
                return new TypeReference<DynamicArray<Uint184>>(isIndexed) {
                };
            case 49:
                return new TypeReference<DynamicArray<Int184>>(isIndexed) {
                };
            case 50:
                return new TypeReference<DynamicArray<Uint192>>(isIndexed) {
                };
            case 51:
                return new TypeReference<DynamicArray<Int192>>(isIndexed) {
                };
            case 52:
                return new TypeReference<DynamicArray<Uint200>>(isIndexed) {
                };
            case 53:
                return new TypeReference<DynamicArray<Int200>>(isIndexed) {
                };
            case 54:
                return new TypeReference<DynamicArray<Uint208>>(isIndexed) {
                };
            case 55:
                return new TypeReference<DynamicArray<Int208>>(isIndexed) {
                };
            case 56:
                return new TypeReference<DynamicArray<Uint216>>(isIndexed) {
                };
            case 57:
                return new TypeReference<DynamicArray<Int216>>(isIndexed) {
                };
            case 58:
                return new TypeReference<DynamicArray<Uint224>>(isIndexed) {
                };
            case 59:
                return new TypeReference<DynamicArray<Int224>>(isIndexed) {
                };
            case 60:
                return new TypeReference<DynamicArray<Uint232>>(isIndexed) {
                };
            case 61:
                return new TypeReference<DynamicArray<Int232>>(isIndexed) {
                };
            case 62:
                return new TypeReference<DynamicArray<Uint240>>(isIndexed) {
                };
            case 63:
                return new TypeReference<DynamicArray<Int240>>(isIndexed) {
                };
            case 64:
                return new TypeReference<DynamicArray<Uint248>>(isIndexed) {
                };
            case 65:
                return new TypeReference<DynamicArray<Int248>>(isIndexed) {
                };
            case 66:
                return new TypeReference<DynamicArray<Uint256>>(isIndexed) {
                };
            case 67:
                return new TypeReference<DynamicArray<Int256>>(isIndexed) {
                };
            case 68:
                return new TypeReference<DynamicArray<Bytes1>>(isIndexed) {
                };
            case 69:
                return new TypeReference<DynamicArray<Bytes2>>(isIndexed) {
                };
            case 70:
                return new TypeReference<DynamicArray<Bytes3>>(isIndexed) {
                };
            case 71:
                return new TypeReference<DynamicArray<Bytes4>>(isIndexed) {
                };
            case 72:
                return new TypeReference<DynamicArray<Bytes5>>(isIndexed) {
                };
            case 73:
                return new TypeReference<DynamicArray<Bytes6>>(isIndexed) {
                };
            case 74:
                return new TypeReference<DynamicArray<Bytes7>>(isIndexed) {
                };
            case 75:
                return new TypeReference<DynamicArray<Bytes8>>(isIndexed) {
                };
            case 76:
                return new TypeReference<DynamicArray<Bytes9>>(isIndexed) {
                };
            case 77:
                return new TypeReference<DynamicArray<Bytes10>>(isIndexed) {
                };
            case 78:
                return new TypeReference<DynamicArray<Bytes11>>(isIndexed) {
                };
            case 79:
                return new TypeReference<DynamicArray<Bytes12>>(isIndexed) {
                };
            case 80:
                return new TypeReference<DynamicArray<Bytes13>>(isIndexed) {
                };
            case 81:
                return new TypeReference<DynamicArray<Bytes14>>(isIndexed) {
                };
            case 82:
                return new TypeReference<DynamicArray<Bytes15>>(isIndexed) {
                };
            case 83:
                return new TypeReference<DynamicArray<Bytes16>>(isIndexed) {
                };
            case 84:
                return new TypeReference<DynamicArray<Bytes17>>(isIndexed) {
                };
            case 85:
                return new TypeReference<DynamicArray<Bytes18>>(isIndexed) {
                };
            case 86:
                return new TypeReference<DynamicArray<Bytes19>>(isIndexed) {
                };
            case 87:
                return new TypeReference<DynamicArray<Bytes20>>(isIndexed) {
                };
            case 88:
                return new TypeReference<DynamicArray<Bytes21>>(isIndexed) {
                };
            case 89:
                return new TypeReference<DynamicArray<Bytes22>>(isIndexed) {
                };
            case 90:
                return new TypeReference<DynamicArray<Bytes23>>(isIndexed) {
                };
            case 91:
                return new TypeReference<DynamicArray<Bytes24>>(isIndexed) {
                };
            case 92:
                return new TypeReference<DynamicArray<Bytes25>>(isIndexed) {
                };
            case 93:
                return new TypeReference<DynamicArray<Bytes26>>(isIndexed) {
                };
            case 94:
                return new TypeReference<DynamicArray<Bytes27>>(isIndexed) {
                };
            case 95:
                return new TypeReference<DynamicArray<Bytes28>>(isIndexed) {
                };
            case 96:
                return new TypeReference<DynamicArray<Bytes29>>(isIndexed) {
                };
            case 97:
                return new TypeReference<DynamicArray<Bytes30>>(isIndexed) {
                };
            case 98:
                return new TypeReference<DynamicArray<Bytes31>>(isIndexed) {
                };
            case 99:
                return new TypeReference<DynamicArray<Bytes32>>(isIndexed) {
                };
            default:
                throw new UnsupportedOperationException("Unsupported type encountered: " + type);
        }
    }
}

