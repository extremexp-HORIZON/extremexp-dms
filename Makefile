DMS_DIR := ./eu.extremexp.dms
FRAMEWORK_DIR := ./extremexp-dsl-framework

.PHONY: all check-maven framework dms clean

all: check-maven framework dms

check-maven:
	@command -v mvn >/dev/null 2>&1 || { echo >&2 "Maven is not installed. Please install Maven and try again."; exit 1; }

framework:
	git submodule update --init --recursive $(FRAMEWORK_DIR)
	cd $(FRAMEWORK_DIR)/eu.extremexp.dsl.parent && mvn clean install

dms:
	cd $(DMS_DIR) && mvn clean install
	cp $(DMS_DIR)/target/*.jar .

clean:
	@echo "Cleaning framework project..."
	cd $(FRAMEWORK_DIR)/eu.extremexp.dsl.parent && mvn clean
	@echo "Cleaning DMS project..."
	cd $(DMS_DIR) && mvn clean
	@echo "Removing copied JAR files..."
	rm -f *.jar

