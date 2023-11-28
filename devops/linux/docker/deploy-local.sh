# #!/bin/bash
# Set JEMPI_HOME environment variable
cd ../../..
export JEMPI_HOME=$(pwd)
export JAVA_VERSION=17.0.8.1-tem
echo "Setting JEMPI_HOME to: $JEMPI_HOME"
JEMPI_CONFIGURATION_PATH=$JEMPI_HOME/JeMPI_Apps/JeMPI_Configuration/ethiopia/config-ethiopia-3.json
# JEMPI_CONFIGURATION_PATH=$JEMPI_HOME/JeMPI_Apps/JeMPI_Configuration/reference/config-reference.json

# Display menu options
echo "Select an option for local deployment:"
echo "1. Deploy JeMPI from Scratch (With all installations...)."
echo "2. Build and Reboot."
echo "3. Restart JeMPI."
echo "4. Down the JeMPI."
echo "5. Destroy JeMPI (This process will wipe all data)."


# Prompt user for choice
read -p "Enter your choice (1-5): " choice


install_docker() {
    if command -v docker &> /dev/null; then
        echo "Docker is already installed."
    else
        # Install Docker
        echo "Installing Docker... "
        sudo apt-get update
        sudo apt-get install -y docker.io

        # Add your user to the docker group to run Docker without sudo
        sudo usermod -aG docker $USER

        echo "Docker has been installed."
    fi
}

# Function to install SDKMAN, Java, Maven, and SBT
install_sdkman_and_java_sbt_maven() {
    # Install SDKMAN
    echo "Installing SDKMAN... "
    curl -s "https://get.sdkman.io" | bash

    # Initialize SDKMAN
    source "$HOME/.sdkman/bin/sdkman-init.sh"

    # Install Java, Maven, and SBT using SDKMAN
    echo "Installing Java : $JAVA_VERSION ... "
    sdk install java $JAVA_VERSION
    sdk use java $JAVA_VERSION

    echo "Installing maven... "
    sdk install maven

    echo "Installing sbt... "
    sdk install sbt
}
run_enviroment_configuration_and_helper_script(){
    # Navigate to environment configuration directory
    echo "Navigate to environment configuration directory"
    cd $JEMPI_HOME/devops/linux/docker/conf/env/
    source $JEMPI_HOME/devops/linux/docker/conf/env/create-env-linux-low-1.sh

    # Running Docker helper scripts 
    echo "Running Docker helper scripts "
    cd $JEMPI_HOME/devops/linux/docker/helper/scripts/
    source $JEMPI_HOME/devops/linux/docker/helper/scripts/x-swarm-a-set-insecure-registries.sh
}

run_field_configuration_file() {
    # Running Docker helper scripts
    echo "Running JeMPI configuration with path: $JEMPI_CONFIGURATION_PATH"
    cd $JEMPI_HOME/JeMPI_Apps/JeMPI_Configuration/
    source $JEMPI_HOME/JeMPI_Apps/JeMPI_Configuration/create.sh $JEMPI_CONFIGURATION_PATH   
}

initialize_swarm(){
    if docker info | grep -q "Swarm: active"; then
        echo "Docker Swarm is running."
    else
        echo "Docker Swarm is not running."
        echo "Initialize Swarm on node1"
         cd $JEMPI_HOME/devops/linux/docker
        source $JEMPI_HOME/devops/linux/docker/b-swarm-1-init-node1.sh

    fi
}

pull_docker_images_and_push_local(){
    # Navigate to Docker directory
    cd $JEMPI_HOME/devops/linux/docker
    echo "Create Docker registry"
    source $JEMPI_HOME/devops/linux/docker/c-registry-1-create.sh

    # Create Docker registry
    if docker images registry | grep -q 'registry'; then
        echo "Image exists."
    else
        echo "Image doesn't exist. Creating and pushing..."
        # Your logic to create and push the image goes here
    fi
    # Pull Docker images from hub
    echo "Pull Docker images from hub"
    source $JEMPI_HOME/devops/linux/docker/a-images-1-pull-from-hub.sh
    # Push Docker images to the registry
    echo "Push Docker images to the registry"
    source $JEMPI_HOME/devops/linux/docker/c-registry-2-push-hub-images.sh
}
build_all_stack_and_reboot(){
    # Build and reboot the entire stack
    echo "Build and reboot the entire stack"
    cd $JEMPI_HOME/devops/linux/docker
    yes | source $JEMPI_HOME/devops/linux/docker/d-stack-1-build-all-reboot.sh

}



# Process user choice
case $choice in
    1)
        echo "Deploy JeMPI from Scratch"
        install_docker
        install_sdkman_and_java_sbt_maven
        run_enviroment_configuration_and_helper_script
        run_field_configuration_file
        initialize_swarm
        pull_docker_images_and_push_local
        build_all_stack_and_reboot
        ;;
    2)
        echo "Build and Reboot"
        build_all_stack_and_reboot
        ;;
    3)
        echo "Restart JeMPI"
        cd $JEMPI_HOME/devops/linux/docker
        source $JEMPI_HOME/devops/linux/docker/d-stack-3-reboot.sh
        # Add your Option 3 logic here
        ;;
    4)
        echo "Down"
        cd $JEMPI_HOME/devops/linux/docker
        source $JEMPI_HOME/devops/linux/docker/d-stack-3-down.sh
        exit 0
        ;;
    5)
        echo "Destroy"
        cd $JEMPI_HOME/devops/linux/docker
        source $JEMPI_HOME/devops/linux/docker/b-swarm-2-leave.sh

        exit 0
        ;;
    *)
        echo "Invalid choice. Please enter a number."
        ;;
esac


