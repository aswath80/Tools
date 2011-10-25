/**
 * RimDatatypeFormatter.java
 * Creation Date: Aug 13, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.query.rim;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import oracle.apps.ctb.configuration.OIDService;
import oracle.apps.ctb.fwk.base.common.CTBException;
import oracle.apps.ctb.fwk.serviceLocator.common.ServiceLocator;
import oracle.apps.ctb.hl7.types.AD;
import oracle.apps.ctb.hl7.types.ADXP;
import oracle.apps.ctb.hl7.types.ANY;
import oracle.apps.ctb.hl7.types.BAG_AD;
import oracle.apps.ctb.hl7.types.BAG_EN;
import oracle.apps.ctb.hl7.types.BAG_TEL;
import oracle.apps.ctb.hl7.types.BL;
import oracle.apps.ctb.hl7.types.CD;
import oracle.apps.ctb.hl7.types.CE;
import oracle.apps.ctb.hl7.types.CS;
import oracle.apps.ctb.hl7.types.ED;
import oracle.apps.ctb.hl7.types.EN;
import oracle.apps.ctb.hl7.types.ENXP;
import oracle.apps.ctb.hl7.types.GTS;
import oracle.apps.ctb.hl7.types.II;
import oracle.apps.ctb.hl7.types.INT;
import oracle.apps.ctb.hl7.types.IVL_INT;
import oracle.apps.ctb.hl7.types.IVL_PQ;
import oracle.apps.ctb.hl7.types.IVL_TS;
import oracle.apps.ctb.hl7.types.LIST_INT;
import oracle.apps.ctb.hl7.types.PQ;
import oracle.apps.ctb.hl7.types.RTO_INT;
import oracle.apps.ctb.hl7.types.RTO_MO_PQ;
import oracle.apps.ctb.hl7.types.RTO_PQ;
import oracle.apps.ctb.hl7.types.SET_CD;
import oracle.apps.ctb.hl7.types.SET_CE;
import oracle.apps.ctb.hl7.types.SET_CS;
import oracle.apps.ctb.hl7.types.SET_II;
import oracle.apps.ctb.hl7.types.SET_PQ;
import oracle.apps.ctb.hl7.types.SET_RTO_PQ;
import oracle.apps.ctb.hl7.types.SET_ST;
import oracle.apps.ctb.hl7.types.ST;
import oracle.apps.ctb.hl7.types.TEL;
import oracle.apps.ctb.hl7.types.common.BAGCommonImpl;
import oracle.apps.ctb.hl7.types.common.LISTAdapterCommonImpl;
import oracle.apps.ctb.hl7.types.common.SETCommonImpl;
import oracle.apps.ctb.hl7.types.common.TYPEImpl;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class RimDatatypeFormatter
{
	private Map<String, FormatterCommand> formatterMap = new HashMap<String, FormatterCommand>();
	private static final String nullString = "<Null:NI>";
	private String internalRoot;
	private Map<String, String> addrPartTypeCodeMap = new HashMap<String, String>()
	{
		{
			put("ADL", "additional locator");
			put("BNN", "building number numeric");
			put("BNR", "building number");
			put("BNS", "building number suffix");
			put("CAR", "care of");
			put("CEN", "census tract");
			put("CNT", "country");
			put("CPA", "county or parish");
			put("CTY", "municipality");
			put("DAL", "delivery address line");
			put("DEL", "delimiter");
			put("DINST", "delivery installation type");
			put("DINSTA", "delivery installation area");
			put("DINSTQ", "delivery installation qualifier");
			put("DIR", "direction");
			put("DMOD", "delivery mode");
			put("DMODID", "delivery mode identifier");
			put("POB", "post box");
			put("PRE", "precinct");
			put("SAL", "street address line");
			put("STA", "state or province");
			put("STB", "street name base");
			put("STR", "street name");
			put("STTYP", "street type");
			put("UNID", "unit identifier");
			put("UNIT", "unit designator");
			put("ZIP", "postal code");
		}
	};
	private Map<String, String> namePartTypeCodeMap = new HashMap<String, String>()
	{
		{
			put("DEL", "delimiter");
			put("FAM", "family");
			put("GIV", "given");
			put("PFX", "prefix");
			put("SFX", "suffix");
		}
	};
	private Map<String, String> nameUseCodeMap = new HashMap<String, String>()
	{
		{
			put("A", "Artist/Stage");
			put("AB", "Abbreviated");
			put("ABC", "Alphabetic");
			put("AL", "Alias");
			put("ASGN", "ASGN");
			put("C", "License");
			put("I", "Indigenous/Tribal");
			put("IDE", "Ideographic");
			put("L", "Legal");
			put("MMN", "Mothers Maiden Name");
			put("OR", "OR");
			put("P", "P");
			put("PHON", "PHON");
			put("R", "Religious");
			put("SNDX", "SNDX");
			put("SRCH", "SRCH");
			put("SYL", "Syllabic");
			put("UNK", "Unknown");
		}
	};
	private Map<String, String> addrUseCodeMap = new HashMap<String, String>()
	{
		{
			put("ABC", "Alphabetic");
			put("BAD", "bad address");
			put("BIR", "birthplace");
			put("DIR", "DIR");
			put("H", "home address");
			put("HP", "primary home");
			put("HV", "vacation home");
			put("IDE", "Ideographic");
			put("PHYS", "physical visit address");
			put("PST", "postal address");
			put("PUB", "PUB");
			put("SYL", "Syllabic");
			put("TMP", "temporary address");
			put("WP", "work place");
		}
	};
	private Map<String, String> telUseCodeMap = new HashMap<String, String>()
	{
		{
			put("AS", "answering service");
			put("BAD", "bad address");
			put("BIR", "birthplace");
			put("DIR", "DIR");
			put("EC", "emergency contact");
			put("H", "home address");
			put("HP", "primary home");
			put("HV", "vacation home");
			put("MC", "mobile contact");
			put("PG", "pager");
			put("PHYS", "physical visit address");
			put("PST", "postal address");
			put("PUB", "PUB");
			put("TMP", "temporary address");
			put("WP", "work place");
		}
	};
	
	private interface FormatterCommand
	{
		void execute(String attributeName, Map<String, String> attributeMap,
		         ANY any);
	}
	
	public RimDatatypeFormatter(ServiceLocator serviceLocator)
	{
		registerFormatters();
		try
		{
			setInternalRoot(serviceLocator);
		}
		catch (CTBException e)
		{
			throw new RuntimeException("Failed to get internal root: " +
			         e.toString());
		}
	}
	
	/**
	 * Method setInternalRoot
	 * 
	 * @param serviceLocator
	 * @throws CTBException
	 */
	private void setInternalRoot(ServiceLocator serviceLocator)
	         throws CTBException
	{
		if (serviceLocator != null)
		{
			internalRoot = serviceLocator.getOIDService()
			         .getOID(OIDService.INTERNAL_ROOT).getRootId().stringValue();
		}
	}
	
	public boolean isFormattingSupportedForDatatype(ANY any)
	{
		return formatterMap.containsKey(any.dataType().shortName().code()
		         .stringValue());
	}
	
	private void registerFormatters()
	{
		formatterMap.put(TYPEImpl.SET_II.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatSET_II(attributeName, attributeMap, (SET_II) any);
			         }
		         });
		formatterMap.put(TYPEImpl.SET_CD.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatSET_CD(attributeName, attributeMap, (SET_CD) any);
			         }
		         });
		formatterMap.put(TYPEImpl.SET_CE.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatSET_CE(attributeName, attributeMap, (SET_CE) any);
			         }
		         });
		formatterMap.put(TYPEImpl.SET_CS.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatSET_CS(attributeName, attributeMap, (SET_CS) any);
			         }
		         });
		formatterMap.put(TYPEImpl.BAG_AD.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatBAG_AD(attributeName, attributeMap, (BAG_AD) any);
			         }
		         });
		formatterMap.put(TYPEImpl.BAG_EN.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatBAG_EN(attributeName, attributeMap, (BAG_EN) any);
			         }
		         });
		formatterMap.put(TYPEImpl.GTS.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatGTS(attributeName, attributeMap, (GTS) any);
			         }
		         });
		formatterMap.put(TYPEImpl.IVL_TS.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatIVL_TS(attributeName, attributeMap, (IVL_TS) any);
			         }
		         });
		formatterMap.put(TYPEImpl.CD.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatCD(attributeName, attributeMap, (CD) any);
			         }
		         });
		formatterMap.put(TYPEImpl.CE.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatCE(attributeName, attributeMap, (CE) any);
			         }
		         });
		formatterMap.put(TYPEImpl.CS.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatCS(attributeName, attributeMap, (CS) any);
			         }
		         });
		formatterMap.put(TYPEImpl.BL.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatBL(attributeName, attributeMap, (BL) any);
			         }
		         });
		formatterMap.put(TYPEImpl.ED.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatED(attributeName, attributeMap, (ED) any);
			         }
		         });
		formatterMap.put(TYPEImpl.BAG_TEL.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatBAG_TEL(attributeName, attributeMap, (BAG_TEL) any);
			         }
		         });
		formatterMap.put(TYPEImpl.IVL_PQ.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatIVL_PQ(attributeName, attributeMap, (IVL_PQ) any);
			         }
		         });
		formatterMap.put(TYPEImpl.SET_PQ.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatSET_PQ(attributeName, attributeMap, (SET_PQ) any);
			         }
		         });
		formatterMap.put(TYPEImpl.PQ.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatPQ(attributeName, attributeMap, (PQ) any);
			         }
		         });
		formatterMap.put(TYPEImpl.RTO_PQ.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatRTO_PQ(attributeName, attributeMap, (RTO_PQ) any);
			         }
		         });
		formatterMap.put(TYPEImpl.SET_RTO_PQ.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatSET_RTO_PQ(attributeName, attributeMap,
				                  (SET_RTO_PQ) any);
			         }
		         });
		formatterMap.put(TYPEImpl.IVL_INT.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatIVL_INT(attributeName, attributeMap, (IVL_INT) any);
			         }
		         });
		formatterMap.put(TYPEImpl.INT.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatINT(attributeName, attributeMap, (INT) any);
			         }
		         });
		formatterMap.put(TYPEImpl.LIST_INT.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatLIST_INT(attributeName, attributeMap, (LIST_INT) any);
			         }
		         });
		formatterMap.put(TYPEImpl.SET_ST.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatSET_ST(attributeName, attributeMap, (SET_ST) any);
			         }
		         });
		formatterMap.put(TYPEImpl.RTO_MO_PQ.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatRTO_MO_PQ(attributeName, attributeMap,
				                  (RTO_MO_PQ) any);
			         }
		         });
		formatterMap.put(TYPEImpl.RTO_INT.shortName().code().stringValue(),
		         new FormatterCommand()
		         {
			         @Override
			         public void execute(String attributeName,
			                  Map<String, String> attributeMap, ANY any)
			         {
				         formatRTO_INT(attributeName, attributeMap, (RTO_INT) any);
			         }
		         });
	}
	
	public void formatRimDatatypeAndPopulate(String attributeName,
	         Map<String, String> attributeMap, ANY any)
	{
		System.out.println("Format called for " + attributeName);
		if (formatterMap.containsKey(any.dataType().shortName().code()
		         .stringValue()))
		{
			System.out.println("YES ***");
			formatterMap.get(any.dataType().shortName().code().stringValue())
			         .execute(attributeName, attributeMap, any);
		}
	}
	
	private boolean isNullVerified(String attributeName,
	         Map<String, String> attributeMap, ANY any)
	{
		boolean isNull = attributeName == null || attributeMap == null ||
		         any == null || any.isNull().isTrue();
		if (any instanceof SETCommonImpl)
		{
			isNull = isNull || ((SETCommonImpl) any).isEmpty().isTrue();
		}
		if (any instanceof BAGCommonImpl)
		{
			isNull = isNull || ((BAGCommonImpl) any).isEmpty().isTrue();
		}
		if (any instanceof LISTAdapterCommonImpl)
		{
			isNull = isNull || ((LISTAdapterCommonImpl) any).isEmpty().isTrue();
		}
		if (isNull)
		{
			attributeMap.put(attributeName, nullString);
		}
		return !isNull;
	}
	
	private void formatSET_II(String attributeName,
	         Map<String, String> attributeMap, SET_II set_ii)
	{
		if (isNullVerified(attributeName, attributeMap, set_ii))
		{
			int index = 1;
			for (Object o : set_ii.toSet())
			{
				II ii = (II) o;
				if (ii.root().stringValue().equals(internalRoot))
				{
					if (ii.extension().isNull().isFalse())
					{
						attributeMap.put(attributeName + "(" + index + ")" +
						         " (Internal)", ii.root().stringValue() + " , " +
						         ii.extension().stringValue());
					}
					else
					{
						attributeMap.put(attributeName + "(" + index + ")" +
						         " (Internal)", ii.root().stringValue());
					}
				}
				else
				{
					if (ii.extension().isNull().isFalse())
					{
						attributeMap.put(attributeName + "(" + index + ")", ii.root()
						         .stringValue() +
						         " , " +
						         ii.extension().stringValue());
					}
					else
					{
						attributeMap.put(attributeName + "(" + index + ")", ii.root()
						         .stringValue());
					}
				}
				index++;
			}
		}
	}
	
	private void formatSET_CD(String attributeName,
	         Map<String, String> attributeMap, SET_CD set_cd)
	{
		if (isNullVerified(attributeName, attributeMap, set_cd))
		{
			int index = 1;
			for (Object o : set_cd.toSet())
			{
				CD cd = (CD) o;
				attributeMap
				         .put(attributeName + "(" + index + ")", cd.code()
				                  .stringValue() +
				                  " , " +
				                  cd.codeSystemName().stringValue());
				index++;
			}
		}
	}
	
	private void formatSET_CE(String attributeName,
	         Map<String, String> attributeMap, SET_CE set_ce)
	{
		if (isNullVerified(attributeName, attributeMap, set_ce))
		{
			int index = 1;
			for (Object o : set_ce.toSet())
			{
				CE ce = (CE) o;
				attributeMap
				         .put(attributeName + "(" + index + ")", ce.code()
				                  .stringValue() +
				                  " , " +
				                  ce.codeSystemName().stringValue());
				index++;
			}
		}
	}
	
	private void formatCD(String attributeName,
	         Map<String, String> attributeMap, CD cd)
	{
		if (isNullVerified(attributeName, attributeMap, cd))
		{
			attributeMap.put(attributeName, cd.code().stringValue() + " , " +
			         cd.codeSystemName().stringValue());
		}
	}
	
	private void formatCE(String attributeName,
	         Map<String, String> attributeMap, CE ce)
	{
		if (isNullVerified(attributeName, attributeMap, ce))
		{
			attributeMap.put(attributeName, ce.code().stringValue() + " , " +
			         ce.codeSystemName().stringValue());
		}
	}
	
	private void formatCS(String attributeName,
	         Map<String, String> attributeMap, CS cs)
	{
		if (isNullVerified(attributeName, attributeMap, cs))
		{
			attributeMap.put(attributeName, cs.code().stringValue());
		}
	}
	
	private void formatSET_CS(String attributeName,
	         Map<String, String> attributeMap, SET_CS set_cs)
	{
		if (isNullVerified(attributeName, attributeMap, set_cs))
		{
			int index = 1;
			for (Object o : set_cs.toSet())
			{
				CS cs = (CS) o;
				attributeMap
				         .put(attributeName + "(" + index + ")", cs.code()
				                  .stringValue() +
				                  " , " +
				                  cs.codeSystemName().stringValue());
				index++;
			}
		}
	}
	
	private void formatBL(String attributeName,
	         Map<String, String> attributeMap, BL bl)
	{
		if (isNullVerified(attributeName, attributeMap, bl))
		{
			attributeMap.put(attributeName, String.valueOf(bl.isTrue()));
		}
	}
	
	private void formatBAG_AD(String attributeName,
	         Map<String, String> attributeMap, BAG_AD bag_ad)
	{
		if (isNullVerified(attributeName, attributeMap, bag_ad))
		{
			int index = 1;
			for (Object o : bag_ad.toList())
			{
				AD ad = (AD) o;
				formatAD(index, attributeName, attributeMap, ad);
				index++;
			}
		}
	}
	
	private void formatAD(int index, String attributeName,
	         Map<String, String> attributeMap, AD ad)
	{
		if (isNullVerified(attributeName, attributeMap, ad))
		{
			Iterator<ADXP> adxpIter = ad.listIterator();
			while (adxpIter.hasNext())
			{
				ADXP adxp = (ADXP) adxpIter.next();
				if (adxp.partType().isNull().isFalse())
				{
					String use = getAddrUse(ad);
					attributeMap.put(
					         attributeName +
					                  "(" +
					                  index +
					                  ").[" +
					                  use +
					                  "]." +
					                  addrPartTypeCodeMap.get(adxp.partType().code()
					                           .stringValue()), adxp.literal()
					                  .stringValue());
				}
			}
		}
	}
	
	private void formatBAG_EN(String attributeName,
	         Map<String, String> attributeMap, BAG_EN bag_en)
	{
		if (isNullVerified(attributeName, attributeMap, bag_en))
		{
			int index = 1;
			for (Object o : bag_en.toList())
			{
				EN en = (EN) o;
				formatEN(index, attributeName, attributeMap, en);
				index++;
			}
		}
	}
	
	private void formatEN(int index, String attributeName,
	         Map<String, String> attributeMap, EN en)
	{
		if (isNullVerified(attributeName, attributeMap, en))
		{
			Iterator<ENXP> adxpIter = en.listIterator();
			while (adxpIter.hasNext())
			{
				ENXP enxp = (ENXP) adxpIter.next();
				if (enxp.partType().isNull().isFalse())
				{
					String use = getNameUse(en);
					attributeMap.put(
					         attributeName +
					                  "(" +
					                  index +
					                  ").[" +
					                  use +
					                  "]." +
					                  namePartTypeCodeMap.get(enxp.partType().code()
					                           .stringValue()), enxp.literal()
					                  .stringValue());
				}
			}
		}
	}
	
	private String getNameUse(EN en)
	{
		StringBuilder sb = new StringBuilder();
		SET_CS set_cs = en.use();
		if (set_cs != null && set_cs.isNull().isFalse() &&
		         set_cs.isEmpty().isFalse())
		{
			Iterator<CS> useIter = set_cs.toSet().iterator();
			while (useIter.hasNext())
			{
				sb.append(nameUseCodeMap.get(useIter.next().code().stringValue()) +
				         ",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	private String getAddrUse(AD ad)
	{
		StringBuilder sb = new StringBuilder();
		SET_CS set_cs = ad.use();
		if (set_cs != null && set_cs.isNull().isFalse() &&
		         set_cs.isEmpty().isFalse())
		{
			Iterator<CS> useIter = set_cs.toSet().iterator();
			while (useIter.hasNext())
			{
				sb.append(addrUseCodeMap.get(useIter.next().code().stringValue()) +
				         ",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	private void formatGTS(String attributeName,
	         Map<String, String> attributeMap, GTS gts)
	{
		if (isNullVerified(attributeName, attributeMap, gts))
		{
			attributeMap.put(attributeName, gts.literal().stringValue());
		}
	}
	
	private void formatIVL_TS(String attributeName,
	         Map<String, String> attributeMap, IVL_TS ivl_ts)
	{
		if (isNullVerified(attributeName, attributeMap, ivl_ts))
		{
			attributeMap.put(attributeName, "low=" +
			         ivl_ts.low().literal().stringValue() + " , high=" +
			         ivl_ts.high().literal().stringValue());
		}
	}
	
	private void formatED(String attributeName,
	         Map<String, String> attributeMap, ED ed)
	{
		if (isNullVerified(attributeName, attributeMap, ed))
		{
			attributeMap.put(attributeName, "mediaType=" +
			         ed.mediaType().code().stringValue() + " , literal=" +
			         ed.literal().stringValue());
		}
	}
	
	private void formatBAG_TEL(String attributeName,
	         Map<String, String> attributeMap, BAG_TEL bag_tel)
	{
		if (isNullVerified(attributeName, attributeMap, bag_tel))
		{
			int index = 1;
			for (Object o : bag_tel.toList())
			{
				TEL tel = (TEL) o;
				String useCodeString = getTELUse(tel);
				attributeMap.put(attributeName + "(" + index + ").[" +
				         useCodeString + "]", tel.literal().stringValue());
			}
		}
	}
	
	private String getTELUse(TEL tel)
	{
		StringBuilder sb = new StringBuilder();
		SET_CS set_cs = tel.use();
		if (set_cs != null && set_cs.isNull().isFalse() &&
		         set_cs.isEmpty().isFalse())
		{
			Iterator<CS> useIter = set_cs.toSet().iterator();
			while (useIter.hasNext())
			{
				sb.append(telUseCodeMap.get(useIter.next().code().stringValue()) +
				         ",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	private void formatSET_PQ(String attributeName,
	         Map<String, String> attributeMap, SET_PQ set_pq)
	{
		if (isNullVerified(attributeName, attributeMap, set_pq))
		{
			int index = 1;
			for (Object o : set_pq.toSet())
			{
				PQ pq = (PQ) o;
				attributeMap.put(attributeName + "(" + index + ")", pq.value()
				         .stringValue() + " " + pq.unit().code().stringValue());
				index++;
			}
		}
	}
	
	private void formatIVL_PQ(String attributeName,
	         Map<String, String> attributeMap, IVL_PQ ivl_pq)
	{
		if (isNullVerified(attributeName, attributeMap, ivl_pq))
		{
			attributeMap.put(attributeName, "low=" +
			         ivl_pq.low().value().stringValue() + " " +
			         ivl_pq.low().unit().code().stringValue() + " , high=" +
			         ivl_pq.high().value().stringValue() + " " +
			         ivl_pq.high().unit().code().stringValue());
		}
	}
	
	private void formatSET_RTO_PQ(String attributeName,
	         Map<String, String> attributeMap, SET_RTO_PQ set_rto_pq)
	{
		if (isNullVerified(attributeName, attributeMap, set_rto_pq))
		{
			int index = 1;
			for (Object o : set_rto_pq.toSet())
			{
				RTO_PQ rto_pq = (RTO_PQ) o;
				attributeMap.put(attributeName + "(" + index + ")", "numerator=" +
				         rto_pq.numerator().value().stringValue() + " " +
				         rto_pq.numerator().unit().code().stringValue() +
				         " , denominator=" +
				         rto_pq.denominator().value().stringValue() + " " +
				         rto_pq.denominator().unit().code().stringValue());
				index++;
			}
		}
	}
	
	private void formatRTO_PQ(String attributeName,
	         Map<String, String> attributeMap, RTO_PQ rto_pq)
	{
		if (isNullVerified(attributeName, attributeMap, rto_pq))
		{
			attributeMap.put(attributeName, "numerator=" +
			         rto_pq.numerator().value().stringValue() + " " +
			         rto_pq.numerator().unit().code().stringValue() +
			         " , denominator=" +
			         rto_pq.denominator().value().stringValue() + " " +
			         rto_pq.denominator().unit().code().stringValue());
		}
	}
	
	private void formatPQ(String attributeName,
	         Map<String, String> attributeMap, PQ pq)
	{
		attributeMap.put(attributeName, pq.value().stringValue() + " " +
		         pq.unit().code().stringValue());
	}
	
	private void formatLIST_INT(String attributeName,
	         Map<String, String> attributeMap, LIST_INT list_int)
	{
		if (isNullVerified(attributeName, attributeMap, list_int))
		{
			int index = 1;
			Iterator<INT> listIntIter = list_int.listIterator();
			while (listIntIter.hasNext())
			{
				INT inte = listIntIter.next();
				attributeMap.put(attributeName + "(" + index + ")", inte.literal()
				         .stringValue());
				index++;
			}
		}
	}
	
	private void formatIVL_INT(String attributeName,
	         Map<String, String> attributeMap, IVL_INT ivl_int)
	{
		if (isNullVerified(attributeName, attributeMap, ivl_int))
		{
			attributeMap.put(attributeName, "low=" +
			         ivl_int.low().literal().stringValue() + " , high=" +
			         ivl_int.high().literal().stringValue());
		}
	}
	
	private void formatINT(String attributeName,
	         Map<String, String> attributeMap, INT inte)
	{
		if (isNullVerified(attributeName, attributeMap, inte))
		{
			attributeMap.put(attributeName, inte.literal().stringValue());
		}
	}
	
	private void formatSET_ST(String attributeName,
	         Map<String, String> attributeMap, SET_ST set_st)
	{
		if (isNullVerified(attributeName, attributeMap, set_st))
		{
			int index = 1;
			for (Object o : set_st.toSet())
			{
				ST st = (ST) o;
				attributeMap.put(attributeName + "(" + index + ")", st.literal()
				         .stringValue());
				index++;
			}
		}
	}
	
	private void formatRTO_MO_PQ(String attributeName,
	         Map<String, String> attributeMap, RTO_MO_PQ rto_mo_pq)
	{
		if (isNullVerified(attributeName, attributeMap, rto_mo_pq))
		{
			attributeMap.put(attributeName, "numerator=" +
			         rto_mo_pq.numerator().value().stringValue() + " " +
			         rto_mo_pq.numerator().currency().code().stringValue() +
			         " , denominator=" +
			         rto_mo_pq.denominator().value().stringValue() + " " +
			         rto_mo_pq.denominator().unit().code().stringValue());
		}
	}
	
	private void formatRTO_INT(String attributeName,
	         Map<String, String> attributeMap, RTO_INT rto_int)
	{
		if (isNullVerified(attributeName, attributeMap, rto_int))
		{
			attributeMap.put(attributeName, "numerator=" +
			         rto_int.numerator().literal().stringValue() +
			         " , denominator=" +
			         rto_int.denominator().literal().stringValue());
		}
	}
}
