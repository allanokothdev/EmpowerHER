// SPDX-License-Identifier: UNLICENSED
pragma solidity 0.8.9;

import "hardhat/console.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";
import "@openzeppelin/contracts/utils/math/SafeMath.sol";
contract Empower is ReentrancyGuard {

    using Counters for Counters.Counter;
    using SafeMath for uint256;

    Counters.Counter private listingIds;
    Counters.Counter private chamaIds;

    mapping(uint256 => Chama) public chamaList;
    mapping(uint256 => Listing) public listingList;
    mapping(uint256 => mapping(address => Contribution)) public contributionList;
    mapping(uint256 => uint256) totalPendingContribution;
    mapping(address => uint256) pendingContribution;


    address payable public admin;

    struct Listing {
        uint256 id;
        string pic;
        string cert;
        string title;
        string summary;
        string location;
        string category;
        uint256 value;
        address publisher;
        bool availability;
    }

    struct Chama {
        uint256 id;
        uint256 listingId;
        string pic;
        string title;
        string summary;
        address publisher;
        uint256 savings;
        uint256 goal;
        address[] members;
        bool open;
    }

    struct Contribution {
        address walletAddress;
        string pic;
        uint256 savings;
        uint256 goal;
    }

    event ChamaItemCreated(
        uint256 indexed id,
        uint256 listingId,
        string pic,
        string title,
        string summary,
        address publisher,
        uint256 savings,
        uint256 goal,
        address[] members,
        bool open
    );

    event ListingItemCreated(
        uint256 indexed id,
        string pic,
        string cert,
        string title,
        string summary,
        string location,
        string category,
        uint256 value,
        address publisher,
        bool availability
    );

    event newMemberJoin(
        address indexed walletAddress,
        uint256 memberNumber,
        string title
    );

    event newContribution(
        address indexed walletAddress,
        string pic,
        uint256 savings,
        uint256 goal
    );

    constructor() payable {
        admin = payable(msg.sender);
    }

    function getBalance() public view returns (uint256) {
        return address(this).balance;
    }

    function createListing(
        string memory _pic,
        string memory _certificate,
        string memory _title,
        string memory _summary,
        string memory _location,
        string memory _category,
        uint256 _value
    ) public nonReentrant {
        listingIds.increment();
        uint256 newListingId = listingIds.current();
        listingList[newListingId] = Listing(
            newListingId,
            _pic,
            _certificate,
            _title,
            _summary,
            _location,
            _category,
            _value,
            msg.sender,
            false
        );
        emit ListingItemCreated(
            newListingId,
            _pic,
            _certificate,
            _title,
            _summary,
            _location,
            _category,
            _value,
            msg.sender,
            false
        );
    }

    function readListings(string memory _category)
        public
        view
        returns (Listing[] memory)
    {
        uint256 totalItemCount = listingIds.current();
        uint256 itemCount = 0;
        uint256 currentIndex = 0;

        for (uint256 i = 0; i < totalItemCount; i++) {
            if ( keccak256(abi.encodePacked(listingList[i + 1].category)) == keccak256(abi.encodePacked(_category))) {
                itemCount += 1;
            }
        }

        Listing[] memory items = new Listing[](itemCount);
        for (uint256 i = 0; i < totalItemCount; i++) {
            if (
                keccak256(abi.encodePacked(listingList[i + 1].category)) ==
                keccak256(abi.encodePacked(_category))
            ) {
                uint256 currentId = i + 1;
                Listing storage currentItem = listingList[currentId];
                items[currentIndex] = currentItem;
                currentIndex += 1;
            }
        }
        return items;
    }

    function transferListing(uint256 _listingId, address payable _chamaAdmin, uint256 _chamaId) public payable onlyAdmin {

        // Check if the struct exists
        Listing memory listing = listingList[_listingId];
        require(listing.value != 0);

        require(address(this).balance >= msg.value, "Insufficient balance");

        //make payment payable
        address payable currentOwner = payable(listing.publisher);

        //change ownership to the chama admin address
        listingList[_listingId].publisher = _chamaAdmin;

        //change listing status to unavailable
        listingList[_listingId].availability = false;

        //send payment to the listing owner
        currentOwner.transfer(totalPendingContribution[_chamaId]);
    }

    function createChama(
        string memory _pic,
        string memory _profilePic,
        string memory _title,
        string memory _summary,
        uint256 _listingId,
        uint256 _listingValue
    ) public nonReentrant {
        chamaIds.increment();
        uint256 newChamaId = chamaIds.current();
        address[] memory members;
        members[0] = msg.sender;

        chamaList[newChamaId] = Chama(
            newChamaId,
            _listingId,
            _pic,
            _title,
            _summary,
            msg.sender,
            1,
            _listingValue,
            members,
            false
        );

        //Add Contribution Object with 0 savings
        contributionList[newChamaId][msg.sender] = Contribution(msg.sender, _profilePic, 1, _listingValue);

        
        emit ChamaItemCreated(newChamaId, _listingId, _pic, _title, _summary, msg.sender, 1, _listingValue, members, true);

    }

    function readChamas() public view returns (Chama[] memory) {
        uint256 totalItemCount = chamaIds.current();
        uint256 itemCount = 0;
        uint256 currentIndex = 0;

        for (uint256 i = 0; i < totalItemCount; i++) {
            for (uint256 p = 0; p < chamaList[i + 1].members.length; p++){
                if (
                keccak256(abi.encodePacked(chamaList[p + 1].members)) ==
                keccak256(abi.encodePacked(msg.sender))
                ) {
                    itemCount += 1;
                }
            }
        }


        Chama[] memory items = new Chama[](itemCount);
        for (uint256 i = 0; i < totalItemCount; i++) {
            for (uint256 p = 0; p < chamaList[i + 1].members.length; p++){
                 if (
                keccak256(abi.encodePacked(chamaList[p + 1].members)) ==
                keccak256(abi.encodePacked(msg.sender))
                ) {
                    uint256 currentId = i + 1;
                    Chama storage currentItem = chamaList[currentId];
                    items[currentIndex] = currentItem;
                    currentIndex += 1;
                }
            }
        }

        return items;
    }


    function joinChama(uint256 _chamaId, string memory _pic) public {
        // Check if the struct exists
        Chama memory chama = chamaList[_chamaId];
        require(chamaList[_chamaId].id != 0);
        uint256 memberID = chama.members.length;
        chama.members[memberID] = msg.sender;

        //Add Contribution Object with 0 savings
        contributionList[_chamaId][msg.sender] = Contribution(msg.sender, _pic, 0, chama.goal);

        emit newMemberJoin( msg.sender, memberID , chama.title);
    }

    function chamaContribution(uint256 _chamaId, uint256 _value) public payable returns (address sender, string memory title, uint256 amount, uint256 totalMemberSavings, uint256 totalChamaSavings) {
        // Check if the struct exists
        Chama memory chama = chamaList[_chamaId];

        //check whether chama is open
        require(chamaList[_chamaId].open == true);

        //check to ensure that the goal hasn't been met
        require(chamaList[_chamaId].goal >= chamaList[_chamaId].savings);

        chamaList[_chamaId].savings.add(_value);

        contributionList[_chamaId][msg.sender].savings.add(_value);

        payable(address(this)).transfer(msg.value);
    
        return (msg.sender, chama.title, _value, _value, _value);
    }

    function refundContribution(uint256 _chamaId) public {
        // Check if the struct exists
        Chama memory chama = chamaList[_chamaId];
        assert(chamaList[_chamaId].open == true);
        uint256 memberID = chama.members.length;
        chama.members[memberID] = msg.sender;
        emit newMemberJoin( msg.sender, memberID , chama.title);
    }


    modifier onlyAdmin() {
        assert(msg.sender == admin);
        _;
    }

    // Need to have a fallback function for the contract to be able to receive funds
    receive() external payable {}
}
