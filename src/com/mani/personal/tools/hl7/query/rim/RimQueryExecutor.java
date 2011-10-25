/**
 * RimQueryExecutor.java
 * Creation Date: Aug 12, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.query.rim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import oracle.apps.ctb.financial.intelligence.common.FinancialActImpl;
import oracle.apps.ctb.fwk.base.common.CTBException;
import oracle.apps.ctb.fwk.serviceLocator.common.ServiceLocator;
import oracle.apps.ctb.hl7.RimService;
import oracle.apps.ctb.hl7.factories.DataTypeFactory;
import oracle.apps.ctb.hl7.factories.QueryComponentFactory;
import oracle.apps.ctb.hl7.query.ActAttributeCriteria;
import oracle.apps.ctb.hl7.query.ActFetch;
import oracle.apps.ctb.hl7.query.ActRelationshipFetch;
import oracle.apps.ctb.hl7.query.EntityAttributeCriteria;
import oracle.apps.ctb.hl7.query.EntityFetch;
import oracle.apps.ctb.hl7.query.ParticipationFetch;
import oracle.apps.ctb.hl7.query.RoleAttributeCriteria;
import oracle.apps.ctb.hl7.query.RoleFetch;
import oracle.apps.ctb.hl7.query.SetSearchOperator;
import oracle.apps.ctb.hl7.rim.Act;
import oracle.apps.ctb.hl7.rim.ActRelationship;
import oracle.apps.ctb.hl7.rim.Entity;
import oracle.apps.ctb.hl7.rim.Participation;
import oracle.apps.ctb.hl7.rim.Role;
import oracle.apps.ctb.hl7.rim.common.AccessImpl;
import oracle.apps.ctb.hl7.rim.common.AccountImpl;
import oracle.apps.ctb.hl7.rim.common.ActImpl;
import oracle.apps.ctb.hl7.rim.common.ContainerImpl;
import oracle.apps.ctb.hl7.rim.common.ContextStructureImpl;
import oracle.apps.ctb.hl7.rim.common.ControlActImpl;
import oracle.apps.ctb.hl7.rim.common.DeviceImpl;
import oracle.apps.ctb.hl7.rim.common.DeviceTaskImpl;
import oracle.apps.ctb.hl7.rim.common.DiagnosticImageImpl;
import oracle.apps.ctb.hl7.rim.common.DietImpl;
import oracle.apps.ctb.hl7.rim.common.DocumentImpl;
import oracle.apps.ctb.hl7.rim.common.EmployeeImpl;
import oracle.apps.ctb.hl7.rim.common.EntityImpl;
import oracle.apps.ctb.hl7.rim.common.FinancialContractImpl;
import oracle.apps.ctb.hl7.rim.common.FinancialTransactionImpl;
import oracle.apps.ctb.hl7.rim.common.InvoiceElementImpl;
import oracle.apps.ctb.hl7.rim.common.LicensedEntityImpl;
import oracle.apps.ctb.hl7.rim.common.LivingSubjectImpl;
import oracle.apps.ctb.hl7.rim.common.ManufacturedMaterialImpl;
import oracle.apps.ctb.hl7.rim.common.MaterialImpl;
import oracle.apps.ctb.hl7.rim.common.NonPersonLivingSubjectImpl;
import oracle.apps.ctb.hl7.rim.common.ObservationImpl;
import oracle.apps.ctb.hl7.rim.common.OrganizationImpl;
import oracle.apps.ctb.hl7.rim.common.PatientEncounterImpl;
import oracle.apps.ctb.hl7.rim.common.PatientImpl;
import oracle.apps.ctb.hl7.rim.common.PersonImpl;
import oracle.apps.ctb.hl7.rim.common.PlaceImpl;
import oracle.apps.ctb.hl7.rim.common.ProcedureImpl;
import oracle.apps.ctb.hl7.rim.common.PublicHealthCaseImpl;
import oracle.apps.ctb.hl7.rim.common.QueryActImpl;
import oracle.apps.ctb.hl7.rim.common.RoleImpl;
import oracle.apps.ctb.hl7.rim.common.SubstanceAdministrationImpl;
import oracle.apps.ctb.hl7.rim.common.SupplyImpl;
import oracle.apps.ctb.hl7.rim.common.WorkingListImpl;
import oracle.apps.ctb.hl7.types.ANY;
import oracle.apps.ctb.hl7.types.II;

import com.mani.personal.tools.hl7.mappingdocgenerator.ui.UIConstants;
import com.mani.personal.tools.hl7.query.helper.ConnectionConstants;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class RimQueryExecutor
{
	private static final QueryComponentFactory QCF = QueryComponentFactory
	         .getInstance();
	private static final DataTypeFactory DTF = DataTypeFactory.getInstance();
	private static List<String> actRimAttrList = new ArrayList<String>();
	private static List<String> entityRimAttrList = new ArrayList<String>();
	private static List<String> roleRimAttrList = new ArrayList<String>();
	
	private RimDatatypeFormatter rimDatatypeFormatter;
	private ServiceLocator serviceLocator;
	
	public RimQueryExecutor(Properties connProperties)
	{
		try
		{
			serviceLocator = ServiceLocator.getInstance(connProperties);
			String username = connProperties
			         .getProperty(ConnectionConstants.JNDI_USER_NAME);
			String password = connProperties
			         .getProperty(ConnectionConstants.JNDI_USER_PASSWORD);
			serviceLocator.login(username, password);
			rimDatatypeFormatter = new RimDatatypeFormatter(serviceLocator);
			loadRimAttributes();
		}
		catch (CTBException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void loadRimAttributes()
	{
		// Act
		actRimAttrList.addAll(Arrays.asList(ActImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(AccountImpl.valueAttributes));
		actRimAttrList
		         .addAll(Arrays.asList(ContextStructureImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(ControlActImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(DeviceTaskImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(DiagnosticImageImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(DietImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(DocumentImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(FinancialActImpl.valueAttributes));
		actRimAttrList.addAll(Arrays
		         .asList(FinancialContractImpl.valueAttributes));
		actRimAttrList.addAll(Arrays
		         .asList(FinancialTransactionImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(InvoiceElementImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(ObservationImpl.valueAttributes));
		actRimAttrList
		         .addAll(Arrays.asList(PatientEncounterImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(ProcedureImpl.valueAttributes));
		actRimAttrList
		         .addAll(Arrays.asList(PublicHealthCaseImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(QueryActImpl.valueAttributes));
		actRimAttrList.addAll(Arrays
		         .asList(SubstanceAdministrationImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(SupplyImpl.valueAttributes));
		actRimAttrList.addAll(Arrays.asList(WorkingListImpl.valueAttributes));
		
		// Entity
		entityRimAttrList.addAll(Arrays.asList(EntityImpl.valueAttributes));
		entityRimAttrList.addAll(Arrays.asList(ContainerImpl.valueAttributes));
		entityRimAttrList.addAll(Arrays.asList(DeviceImpl.valueAttributes));
		entityRimAttrList
		         .addAll(Arrays.asList(LivingSubjectImpl.valueAttributes));
		entityRimAttrList.addAll(Arrays
		         .asList(ManufacturedMaterialImpl.valueAttributes));
		entityRimAttrList.addAll(Arrays.asList(MaterialImpl.valueAttributes));
		entityRimAttrList.addAll(Arrays
		         .asList(NonPersonLivingSubjectImpl.valueAttributes));
		entityRimAttrList.addAll(Arrays.asList(OrganizationImpl.valueAttributes));
		entityRimAttrList.addAll(Arrays.asList(PersonImpl.valueAttributes));
		entityRimAttrList.addAll(Arrays.asList(PlaceImpl.valueAttributes));
		
		// Role
		roleRimAttrList.addAll(Arrays.asList(RoleImpl.valueAttributes));
		roleRimAttrList.addAll(Arrays.asList(AccessImpl.valueAttributes));
		roleRimAttrList.addAll(Arrays.asList(EmployeeImpl.valueAttributes));
		roleRimAttrList.addAll(Arrays.asList(LicensedEntityImpl.valueAttributes));
		roleRimAttrList.addAll(Arrays.asList(PatientImpl.valueAttributes));
	}
	
	public Map<String, String> executeRimQuery(String rimObjectType,
	         String root, String extn) throws CTBException
	{
		if (UIConstants.ACT_RADIO_COMMAND.equals(rimObjectType))
		{
			return queryRimActs(root, extn);
		}
		else if (UIConstants.ENTITY_RADIO_COMMAND.equals(rimObjectType))
		{
			return queryRimEntities(root, extn);
		}
		else if (UIConstants.ROLE_RADIO_COMMAND.equals(rimObjectType))
		{
			return queryRimRoles(root, extn);
		}
		throw new RuntimeException("Unknown RIM Object Type " + rimObjectType);
	}
	
	private Map<String, String> queryRimActs(String root, String extn)
	         throws CTBException
	{
		Map<String, String> attributeMap = new HashMap<String, String>();
		// Act fetch
		ActAttributeCriteria actAttributeCriteria = QCF.newActAttributeCriteria();
		actAttributeCriteria.setId(SetSearchOperator.ANY,
		         new II[] { DTF.newII(root, extn, false) });
		actAttributeCriteria.setCurrentVersion(true);
		
		ActFetch actFetch = QCF.newActFetch(actAttributeCriteria);
		actFetch.retrieveAll();
		
		// OB AR
		ActRelationshipFetch obARFetch = QCF.newActRelationshipFetch();
		obARFetch.retrieveTypeCode(true);
		// Target Act Fetch
		ActFetch targetActFetch = QCF.newActFetch();
		targetActFetch.retrieveId(true);
		//
		obARFetch.addTargetFetch(targetActFetch);
		actFetch.addOBActRelationshipFetch(obARFetch);
		// IB AR
		ActRelationshipFetch ibARFetch = QCF.newActRelationshipFetch();
		ibARFetch.retrieveTypeCode(true);
		// Source Act Fetch
		ActFetch sourceActFetch = QCF.newActFetch();
		sourceActFetch.retrieveId(true);
		//
		ibARFetch.addSourceFetch(sourceActFetch);
		actFetch.addIBActRelationshipFetch(ibARFetch);
		// Participation
		ParticipationFetch partFetch = QCF.newParticipationFetch();
		partFetch.retrieveTypeCode(true);
		// Role
		RoleFetch roleFetch = QCF.newRoleFetch();
		roleFetch.retrieveId(true);
		//
		partFetch.addRoleFetch(roleFetch);
		actFetch.addParticipationFetch(partFetch);
		// Execute
		RimService rimService = serviceLocator.getRimService();
		long startTime = System.currentTimeMillis();
		Iterator<Act> actIter = rimService.queryActs(serviceLocator, actFetch);
		long endTime = System.currentTimeMillis();
		System.out.println("RIM Query Acts Executed In " + (endTime - startTime) +
		         " ms");
		if (actIter.hasNext())
		{
			populateActAttributesInMap(actIter.next(), attributeMap);
		}
		else
		{
			attributeMap.put("No act found matching II " + root + "/" + extn, "");
		}
		System.out.println("Act.attributeMap = " + attributeMap);
		return attributeMap;
	}
	
	private Map<String, String> queryRimEntities(String root, String extn)
	         throws CTBException
	{
		Map<String, String> attributeMap = new HashMap<String, String>();
		// Entity Fetch
		EntityAttributeCriteria entityAttributeCriteria = QCF
		         .newEntityAttributeCriteria();
		entityAttributeCriteria.setId(SetSearchOperator.ANY,
		         new II[] { DTF.newII(root, extn, false) });
		entityAttributeCriteria.setCurrentVersion(true);
		
		EntityFetch entityFetch = QCF.newEntityFetch(entityAttributeCriteria);
		entityFetch.retrieveAll();
		// PlayedRole
		RoleFetch playedRoleFetch = QCF.newRoleFetch();
		playedRoleFetch.retrieveId(true);
		//
		entityFetch.addPlayedRoleFetch(playedRoleFetch);
		// ScopedRole
		RoleFetch scopedRoleFetch = QCF.newRoleFetch();
		scopedRoleFetch.retrieveId(true);
		//
		entityFetch.addScopedRoleFetch(scopedRoleFetch);
		// Execute
		RimService rimService = serviceLocator.getRimService();
		long startTime = System.currentTimeMillis();
		Iterator<Entity> entityIter = rimService.queryEntities(serviceLocator,
		         entityFetch);
		long endTime = System.currentTimeMillis();
		System.out.println("RIM Query Entities Executed In " +
		         (endTime - startTime) + " ms");
		if (entityIter.hasNext())
		{
			populateEntityAttributesInMap(entityIter.next(), attributeMap);
		}
		else
		{
			attributeMap.put("No entity found matching II " + root + "/" + extn,
			         "");
		}
		System.out.println("Entity.attributeMap = " + attributeMap);
		return attributeMap;
	}
	
	private Map<String, String> queryRimRoles(String root, String extn)
	         throws CTBException
	{
		Map<String, String> attributeMap = new HashMap<String, String>();
		// Role Fetch
		RoleAttributeCriteria roleAttributeCriteria = QCF
		         .newRoleAttributeCriteria();
		roleAttributeCriteria.setId(SetSearchOperator.ANY,
		         new II[] { DTF.newII(root, extn, false) });
		roleAttributeCriteria.setCurrentVersion(true);
		
		RoleFetch roleFetch = QCF.newRoleFetch(roleAttributeCriteria);
		roleFetch.retrieveAll();
		// Player
		EntityFetch playerEntityFetch = QCF.newEntityFetch();
		playerEntityFetch.retrieveId(true);
		//
		roleFetch.addPlayerEntityFetch(playerEntityFetch);
		// Scoper
		EntityFetch scoperEntityFetch = QCF.newEntityFetch();
		scoperEntityFetch.retrieveId(true);
		//
		roleFetch.addScoperEntityFetch(scoperEntityFetch);
		// Participation
		ParticipationFetch partFetch = QCF.newParticipationFetch();
		partFetch.retrieveTypeCode(true);
		// Act
		ActFetch actFetch = QCF.newActFetch();
		actFetch.retrieveId(true);
		//
		partFetch.addActFetch(actFetch);
		roleFetch.addParticipationFetch(partFetch);
		// Execute
		RimService rimService = serviceLocator.getRimService();
		long startTime = System.currentTimeMillis();
		Iterator<Role> roleIter = rimService
		         .queryRoles(serviceLocator, roleFetch);
		long endTime = System.currentTimeMillis();
		System.out.println("RIM Query Roles Executed In " +
		         (endTime - startTime) + " ms");
		if (roleIter.hasNext())
		{
			populateRoleAttributesInMap(roleIter.next(), attributeMap);
		}
		else
		{
			attributeMap.put("No role found matching II " + root + "/" + extn, "");
		}
		System.out.println("Role.attributeMap = " + attributeMap);
		return attributeMap;
	}
	
	/**
	 * Method populateActAttributesInMap
	 * 
	 * @param act
	 * @param attributeMap
	 * @throws CTBException
	 */
	private void populateActAttributesInMap(Act act,
	         Map<String, String> attributeMap) throws CTBException
	{
		ActImpl actImpl = (ActImpl) act;
		
		for (String attributeName : actRimAttrList)
		{
			Object attributeValue = actImpl.getAttribute(attributeName);
			System.out.println("Processing RIM Act attr " + attributeName +
			         " with value " + attributeValue);
			if (attributeValue == null || attributeValue instanceof ANY)
			{
				populateRimAttributesInMap(attributeName, (ANY) attributeValue,
				         attributeMap);
			}
		}
		attributeMap.put(ActImpl.VERSION_NUM,
		         String.valueOf(actImpl.getVersionNum()));
		
		Iterator<ActRelationship> obARIter = act.getOBActRelationships();
		int obARIndex = 1;
		while (obARIter.hasNext())
		{
			ActRelationship obAR = obARIter.next();
			attributeMap.put("OBActRelationship(" + obARIndex + ")", obAR
			         .getTypeCode().toString() +
			         " TargetAct(" +
			         obAR.getTarget().getClassCode() +
			         "," +
			         obAR.getTarget().getMoodCode() +
			         ") : " +
			         obAR.getTarget().getId().toString());
			obARIndex++;
		}
		Iterator<ActRelationship> ibARIter = act.getIBActRelationships();
		int ibARIndex = 1;
		while (ibARIter.hasNext())
		{
			ActRelationship ibAR = ibARIter.next();
			attributeMap.put("IBActRelationship(" + ibARIndex + ")", ibAR
			         .getTypeCode().toString() +
			         " SourceAct(" +
			         ibAR.getSource().getClassCode() +
			         "," +
			         ibAR.getSource().getMoodCode() +
			         ") : " +
			         ibAR.getSource().getId().toString());
			ibARIndex++;
		}
		Iterator<Participation> partIter = act.getParticipations();
		int partIndex = 1;
		while (partIter.hasNext())
		{
			Participation part = partIter.next();
			attributeMap.put("Participation(" + partIndex + ")", part
			         .getTypeCode().toString() +
			         " Role(" +
			         part.getRole().getClassCode() +
			         ") : " +
			         part.getRole().getId().toString());
			partIndex++;
		}
	}
	
	/**
	 * Method populateEntityAttributesInMap
	 * 
	 * @param entity
	 * @param attributeMap
	 * @throws CTBException
	 */
	private void populateEntityAttributesInMap(Entity entity,
	         Map<String, String> attributeMap) throws CTBException
	{
		EntityImpl entityImpl = (EntityImpl) entity;
		
		for (String attributeName : entityRimAttrList)
		{
			Object attributeValue = entityImpl.getAttribute(attributeName);
			System.out.println("Processing RIM Entity attr " + attributeName +
			         " with value " + attributeValue);
			if (attributeValue == null || attributeValue instanceof ANY)
			{
				populateRimAttributesInMap(attributeName, (ANY) attributeValue,
				         attributeMap);
			}
		}
		attributeMap.put(EntityImpl.VERSION_NUM,
		         String.valueOf(entityImpl.getVersionNum()));
		
		Iterator<Role> playedRoleIter = entity.getPlayedRoles();
		int playedRoleIndex = 1;
		while (playedRoleIter.hasNext())
		{
			Role playedRole = playedRoleIter.next();
			attributeMap.put("PlayedRole(" + playedRoleIndex + ")", "(" +
			         playedRole.getClassCode().toString() + "): " +
			         playedRole.getId().toString());
			playedRoleIndex++;
		}
		Iterator<Role> scopedRoleIter = entity.getScopedRoles();
		int scopedRoleIndex = 1;
		while (scopedRoleIter.hasNext())
		{
			Role scopedRole = scopedRoleIter.next();
			attributeMap.put("ScopedRole(" + scopedRoleIndex + ")", "(" +
			         scopedRole.getClassCode().toString() + "): " +
			         scopedRole.getId().toString());
			scopedRoleIndex++;
		}
	}
	
	/**
	 * Method populateRoleAttributesInMap
	 * 
	 * @param role
	 * @param attributeMap
	 * @throws CTBException
	 */
	private void populateRoleAttributesInMap(Role role,
	         Map<String, String> attributeMap) throws CTBException
	{
		RoleImpl roleImpl = (RoleImpl) role;
		
		for (String attributeName : roleRimAttrList)
		{
			Object attributeValue = roleImpl.getAttribute(attributeName);
			System.out.println("Processing RIM Role attr " + attributeName +
			         " with value " + attributeValue);
			if (attributeValue == null || attributeValue instanceof ANY)
			{
				populateRimAttributesInMap(attributeName, (ANY) attributeValue,
				         attributeMap);
			}
		}
		attributeMap.put(RoleImpl.VERSION_NUM,
		         String.valueOf(roleImpl.getVersionNum()));
		
		Entity playerEntity = role.getPlayerEntity();
		if (playerEntity != null)
		{
			attributeMap.put("PlayerEntity", "(" +
			         playerEntity.getClassCode().toString() + "," +
			         playerEntity.getDeterminerCode().toString() + "): " +
			         playerEntity.getId().toString());
		}
		
		Entity scoperEntity = role.getScoperEntity();
		if (scoperEntity != null)
		{
			attributeMap.put("ScoperEntity", "(" +
			         scoperEntity.getClassCode().toString() + "," +
			         scoperEntity.getDeterminerCode().toString() + "): " +
			         scoperEntity.getId().toString());
		}
		
		Iterator<Participation> partIter = role.getParticipations();
		int partIndex = 1;
		while (partIter.hasNext())
		{
			Participation part = partIter.next();
			attributeMap.put("Participation(" + partIndex + ")", part
			         .getTypeCode().toString() +
			         " Act(" +
			         part.getAct().getClassCode() +
			         "," +
			         part.getAct().getMoodCode() +
			         "): " +
			         part.getAct().getId().toString());
			partIndex++;
		}
	}
	
	/**
	 * Method populateRimAttributesInMap
	 * 
	 * @param attributeName
	 * @param any
	 * @param attributeMap
	 */
	private void populateRimAttributesInMap(String attributeName, ANY any,
	         Map<String, String> attributeMap)
	{
		if (any == null)
		{
			attributeMap.put(attributeName, "<Null:NI>");
		}
		else if (rimDatatypeFormatter.isFormattingSupportedForDatatype(any))
		{
			rimDatatypeFormatter.formatRimDatatypeAndPopulate(attributeName,
			         attributeMap, any);
		}
		else
		{
			attributeMap.put(attributeName, any.toString());
		}
	}
}
