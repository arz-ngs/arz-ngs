package at.arz.ngs.journal;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.security.Role;
import at.arz.ngs.security.RoleRepository;

public class JournalAdmin {
	
	@Inject
	private JournalRepository journalRepositoy;
	
	@Inject
	private ServiceInstanceRepository serviceInstanceRepository;
	
	@Inject
	private RoleRepository roleRepository;
	
	protected JournalAdmin() {
		//ejb constructor
	}
	
	public JournalAdmin(JournalRepository journalRepository, ServiceInstanceRepository serviceInstanceRepository, RoleRepository roleRepository) {
		this.journalRepositoy = journalRepository;
		this.serviceInstanceRepository = serviceInstanceRepository;
		this.roleRepository = roleRepository;
	}
	
	public List<JournalResponse>getAllJournalEntries() {
		List<JournalEntry> entries = journalRepositoy.getAllJournalEntries();
		List<JournalResponse> response = new LinkedList<JournalResponse>();
		for (JournalEntry je : entries) {
			JournalResponse jr = new JournalResponse();
			jr.setTimestamp(je.getTimestamp());
			jr.setUserName(je.getUserName().getName());
			jr.setTargetObject_class(je.getTargetObject_class());
			jr.setAction(je.getAction());
			if (je.getTargetObject_class().equals("ServiceInstance")) {
				ServiceInstance si = serviceInstanceRepository.getServiceInstanceByOid(je.getTargetObject_oid());
				jr.setTargetObject(si.toString());
			} else if (je.getTargetObject_class().equals("Role")) {
				Role role = roleRepository.getRoleByOid(je.getTargetObject_oid());
				jr.setTargetObject(role.getRoleName().getName());
			}
			response.add(jr);
			}
		return response;
	}

}
