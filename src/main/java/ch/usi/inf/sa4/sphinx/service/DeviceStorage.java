package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;

@Entity
public interface DeviceStorage extends JpaRepository<Device, Integer> {}


